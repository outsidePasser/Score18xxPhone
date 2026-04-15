"""
Fetches game data from tobymao/18xx and converts it to the game_data.json format
used by Score18xxPhone.

Usage: python fetch_games.py
Output: app/src/main/assets/game_data.json (merges with existing data)
"""

import re
import json
import urllib.request
import urllib.error
import time
import os

GITHUB_RAW = "https://raw.githubusercontent.com/tobymao/18xx/master"

# Named colors used occasionally in the Ruby source
NAMED_COLORS = {
    'white': '#ffffff',
    'black': '#000000',
    'red': '#ff0000',
    'green': '#008000',
    'blue': '#0000ff',
    'yellow': '#ffff00',
    'orange': '#ffa500',
    'purple': '#800080',
    'brown': '#a52a2a',
    'gray': '#808080',
    'grey': '#808080',
}


def fetch_url(url):
    try:
        req = urllib.request.Request(url, headers={'User-Agent': 'Score18xxPhone-fetcher'})
        with urllib.request.urlopen(req, timeout=15) as response:
            return response.read().decode('utf-8')
    except Exception as e:
        print(f"    [fetch error] {url}: {e}")
        return None


def extract_block(content, keyword, open_char='[', close_char=']'):
    """Find 'keyword' in content and return the balanced bracket block that follows."""
    idx = content.find(keyword)
    if idx == -1:
        return None
    open_pos = content.find(open_char, idx)
    if open_pos == -1:
        return None
    depth = 0
    for i in range(open_pos, len(content)):
        c = content[i]
        if c == open_char:
            depth += 1
        elif c == close_char:
            depth -= 1
            if depth == 0:
                return content[open_pos:i + 1]
    return None


def normalize_color(raw):
    """Convert a color value (hex string or named color) to #RRGGBB."""
    if not raw:
        return None
    raw = raw.strip().strip(":'\"")
    if raw.startswith('#'):
        # Expand 3-digit hex to 6-digit
        if len(raw) == 4:
            raw = '#' + ''.join(c * 2 for c in raw[1:])
        return raw.upper() if len(raw) == 7 else raw
    lower = raw.lower()
    return NAMED_COLORS.get(lower)


def parse_corporations(entities_content):
    """Extract major corporations from entities.rb.

    Returns a list of dicts with keys: name, color, textColor.
    Skips corporations whose sym is purely numeric (minor railways).
    """
    block = extract_block(entities_content, 'CORPORATIONS = [')
    if not block:
        block = extract_block(entities_content, 'CORPORATIONS=[')
    if not block:
        return []

    corporations = []
    i = 0
    while i < len(block):
        if block[i] != '{':
            i += 1
            continue
        # Find matching closing brace
        depth = 0
        for j in range(i, len(block)):
            if block[j] == '{':
                depth += 1
            elif block[j] == '}':
                depth -= 1
                if depth == 0:
                    corp_block = block[i:j + 1]

                    sym_m = re.search(r"sym:\s*['\"]([^'\"]+)['\"]", corp_block)
                    if not sym_m:
                        i = j + 1
                        break
                    sym = sym_m.group(1)

                    # Skip minor railways (purely numeric symbols)
                    if sym.isdigit() or re.match(r'^\d+$', sym):
                        i = j + 1
                        break

                    # color: '#RRGGBB'  or  color: :'#RRGGBB'  or  color: 'white'
                    color_m = re.search(
                        r"(?<!\w)color:\s*:?['\"]?([#\w][^'\",\n\r]*?)['\"]?\s*(?:,|\n|$)",
                        corp_block
                    )
                    color = normalize_color(color_m.group(1)) if color_m else None
                    if not color:
                        i = j + 1
                        break

                    # text_color: 'black' / 'white' — default white when absent
                    tc_m = re.search(r"text_color:\s*['\"]?(\w+)['\"]?", corp_block)
                    if tc_m:
                        tc = tc_m.group(1).lower()
                        text_color = '#000000' if tc == 'black' else '#ffffff'
                    else:
                        # Decide default by luminance: dark backgrounds → white text
                        text_color = guess_text_color(color)

                    corporations.append({
                        'name': sym,
                        'color': color,
                        'textColor': text_color,
                    })
                    i = j + 1
                    break
        else:
            i += 1

    return corporations


def guess_text_color(hex_color):
    """Return '#ffffff' or '#000000' based on background luminance."""
    try:
        r = int(hex_color[1:3], 16)
        g = int(hex_color[3:5], 16)
        b = int(hex_color[5:7], 16)
        luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255
        return '#000000' if luminance > 0.5 else '#ffffff'
    except Exception:
        return '#ffffff'


def parse_stock_prices(game_content):
    """Extract unique, positive stock prices from the MARKET constant."""
    block = extract_block(game_content, 'MARKET = [')
    if not block:
        block = extract_block(game_content, 'MARKET=[')
    if not block:
        return []

    prices = set()
    # Handle both quoted strings ('100p', "60y") and unquoted %w[] tokens (100p, 60y)
    for quoted, unquoted in re.findall(r"['\"](\d+)[a-zA-Z]*['\"]|\b(\d+)[a-zA-Z]*\b", block):
        raw = quoted or unquoted
        if raw:
            val = int(raw)
            if val > 0:
                prices.add(val)

    return sorted(prices)


def get_title(folder):
    """g_1830 → '1830', g_1822_ca → '1822CA', g_1836_jr30 → '1836JR30'"""
    name = folder[2:] if folder.startswith('g_') else folder
    parts = name.split('_')
    return parts[0] + ''.join(p.upper() for p in parts[1:])


def process_game(game_folder):
    entities = fetch_url(f"{GITHUB_RAW}/lib/engine/game/{game_folder}/entities.rb")
    game    = fetch_url(f"{GITHUB_RAW}/lib/engine/game/{game_folder}/game.rb")
    meta    = fetch_url(f"{GITHUB_RAW}/lib/engine/game/{game_folder}/meta.rb")

    if not entities or not game:
        print(f"  -> skipped (missing files)")
        return None

    corporations = parse_corporations(entities)
    stock_prices = parse_stock_prices(game)
    title        = get_title(game_folder)

    subtitle_m = re.search(r"GAME_SUBTITLE\s*=\s*['\"]([^'\"]+)['\"]", meta or '')
    full_name  = f"{title}: {subtitle_m.group(1)}" if subtitle_m else title

    if not corporations:
        print(f"  -> skipped (no corporations parsed)")
        return None
    if not stock_prices:
        print(f"  -> skipped (no stock prices parsed)")
        return None

    print(f"  -> {len(corporations)} corps, {len(stock_prices)} price points  [{full_name}]")
    return {
        'title':              title,
        'fullName':           full_name,
        'companies':          corporations,
        'maxSharesPerPlayer': 10,
        'stockPrices':        stock_prices,
    }


# -------------------------------------------------------------------
# All game directories from tobymao/18xx
# -------------------------------------------------------------------
GAMES = [
    'g_1804',
    'g_1807',
    'g_1812',
    'g_1817',
    'g_1817_de',
    'g_1817_na',
    'g_1817_wo',
    'g_1822',
    'g_1822_africa',
    'g_1822_ca',
    'g_1822_ca_ers',
    'g_1822_ca_wrs',
    'g_1822_mrs',
    'g_1822_mx',
    'g_1822_nrs',
    'g_1822_pnw',
    'g_1822_pnw_short',
    'g_1822_pnw_srs',
    'g_1824',
    'g_1824_cisleithania',
    'g_1825',
    'g_1826',
    'g_1828',
    'g_1829',
    'g_1830',
    'g_1832',
    'g_1835',
    'g_1836_jr30',
    'g_1836_jr56',
    'g_1837',
    'g_1840',
    'g_1841',
    'g_1844',
    'g_1846',
    'g_1846_two_player_variant',
    'g_1847_ae',
    'g_1848',
    'g_1849',
    'g_1849_boot',
    'g_1850',
    'g_1850_jr',
    'g_1854',
    'g_1856',
    'g_1858',
    'g_1858_india',
    'g_1858_switzerland',
    'g_1860',
    'g_1861',
    'g_1862',
    'g_1862_solo',
    'g_1866',
    'g_1867',
    'g_1868_wy',
    'g_1870',
    'g_1871',
    'g_1873',
    'g_1877',
    'g_1877_stockholm_tramways',
    'g_1880',
    'g_1880_romania',
    'g_1882',
    'g_1888',
    'g_1889',
]


def main():
    output_path = os.path.join(
        os.path.dirname(__file__),
        'app', 'src', 'main', 'assets', 'game_data.json'
    )

    # Load existing data so we can preserve hand-curated entries
    existing = {}
    if os.path.exists(output_path):
        with open(output_path, 'r') as f:
            for entry in json.load(f):
                existing[entry['title']] = entry
        print(f"Loaded {len(existing)} existing games from game_data.json\n")

    fetched = {}
    for folder in GAMES:
        title = get_title(folder)
        print(f"[{title}]  ({folder})")
        game = process_game(folder)
        if game:
            fetched[title] = game
        time.sleep(0.4)   # be polite to GitHub

    # Merge: hand-curated entries (titles NOT in GAMES list) are preserved as-is.
    # All fetched entries are updated/added.
    fetched_titles = {get_title(f) for f in GAMES}
    merged = {t: e for t, e in existing.items() if t not in fetched_titles}
    added = 0
    for title, game in fetched.items():
        was_new = title not in existing
        merged[title] = game
        if was_new:
            added += 1
            print(f"  [NEW]  {title}")
        else:
            print(f"  [UPD]  {title}")

    # Sort alphabetically by title
    sorted_games = sorted(merged.values(), key=lambda g: g['title'])

    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(sorted_games, f, indent=2, ensure_ascii=False)

    print(f"\nDone. {added} new games added, {len(sorted_games)} total -> {output_path}")


if __name__ == '__main__':
    main()
