# Builds the IQ item bank as a migration script. Items are generated from templates so the whole
# bank is deterministic and reproducible; each template family carries the difficulty (Rasch
# logits) its rules are worth, which the app then recalibrates from real answers.
#
# Figure spec, kept short because Oracle string literals stop at 4000 bytes:
#   s shape (circle|square|triangle|diamond|hex|cross|star), f fill (none|solid|half),
#   n count 1..4, r rotation degrees, z size (s|m|l)
import json

SHAPES = ["circle", "square", "triangle", "diamond", "hex", "cross", "star"]
FILLS = ["none", "solid", "half"]
SIZES = ["s", "m", "l"]

items = []  # (code, type, payload dict, answer index, difficulty)


def fig(shape="circle", fill="none", n=1, rot=0, size="m"):
    return {"s": shape, "f": fill, "n": n, "r": rot, "z": size}


def mutate(f, seed):
    """A plausible wrong option: the right figure with one attribute moved."""
    out = dict(f)
    which = seed % 4
    if which == 0:
        out["s"] = SHAPES[(SHAPES.index(f["s"]) + 1 + seed // 4) % len(SHAPES)]
    elif which == 1:
        out["f"] = FILLS[(FILLS.index(f["f"]) + 1 + seed // 4) % len(FILLS)]
    elif which == 2:
        out["n"] = 1 + ((f["n"] + seed // 4) % 4)
    else:
        out["z"] = SIZES[(SIZES.index(f["z"]) + 1 + seed // 4) % len(SIZES)]
    return out


def options_for(answer, count, seed):
    """The right figure among distractors, each one attribute away from it."""
    opts, used = [answer], {json.dumps(answer, sort_keys=True)}
    step = 0
    while len(opts) < count:
        candidate = mutate(answer, seed + step)
        key = json.dumps(candidate, sort_keys=True)
        if key not in used:
            used.add(key)
            opts.append(candidate)
        step += 1
    # Deterministic shuffle: the right one is not always first.
    index = (seed * 7 + 3) % count
    opts[0], opts[index] = opts[index], opts[0]
    return opts, index


# ---- Matrices: a 3x3 grid with the bottom right cell missing -----------------------------------
# Every active rule is one more thing to hold in mind at once, which is what makes a matrix hard.
MATRIX_DIFFICULTY = {1: -2.2, 2: -1.0, 3: 0.2, 4: 1.4, 5: 2.4}


def matrix(rules, variant):
    # Each variant draws from its own three shapes, so two variants of the same rule set are not
    # the same puzzle twice.
    pool = [SHAPES[(variant * 2 + k) % len(SHAPES)] for k in range(3)]

    def cell(row, col):
        f = fig()
        f["s"] = pool[(col + row) % 3] if "shape" in rules else pool[0]
        f["f"] = FILLS[(row + variant) % 3] if "fill" in rules else "none"
        f["n"] = 1 + ((col + 2 * row) % 3) if "count" in rules else 1
        f["r"] = 45 * ((col + row) % 4) if "rot" in rules else 0
        f["z"] = SIZES[(row + col + variant) % 3] if "size" in rules else "m"
        return f

    cells = [cell(r, c) for r in range(3) for c in range(3)]
    answer = cells[8]
    opts, index = options_for(answer, 6, variant * 3 + len(rules))
    return {"c": cells[:8], "o": opts}, index


ALL_RULES = ["shape", "fill", "count", "rot", "size"]
for count in range(1, 6):
    for variant in range(4):
        rules = ALL_RULES[:count]
        payload, answer = matrix(rules, variant)
        items.append((f"MTX-{count}-{variant}", "MATRIX", payload, answer,
                      MATRIX_DIFFICULTY[count] + 0.15 * (variant - 1.5)))

# ---- Number series ----------------------------------------------------------------------------
# Each pattern is a kind of rule to spot; the difficulty is the pattern's, not the arithmetic's.
SERIES = [
    ("add", -2.4, lambda a, b, i: a + b * i),
    ("step", -1.2, lambda a, b, i: a + b * i * (i + 1) // 2),
    ("mul", -1.0, lambda a, b, i: a * (b ** i)),
    ("alt", -0.2, lambda a, b, i: a + (b + 2) * ((i + 1) // 2) - b * (i // 2)),
    ("square", 0.6, lambda a, b, i: a + (i + b) ** 2),
    ("fib", 1.0, None),
    ("weave", 1.8, None),
    ("quad", 2.2, lambda a, b, i: a + b * i * i + i),
]


def series_terms(name, rule, a, b):
    if name == "fib":
        terms = [a, b]
        while len(terms) < 7:
            terms.append(terms[-1] + terms[-2])
        return terms
    if name == "weave":
        # Two series plaited together: one climbing, one falling.
        return [a + b * (i // 2) if i % 2 == 0 else 40 - b * (i // 2) for i in range(7)]
    return [rule(a, b, i) for i in range(7)]


for name, difficulty, rule in SERIES:
    for variant in range(3):
        a, b = 2 + variant * 3, 2 + variant
        terms = series_terms(name, rule, a, b)
        shown, answer_value = terms[:5], terms[5]
        # Distractors: near misses, the kind a half-spotted rule produces.
        offsets = [1, -1, 2, -3, 4, -5]
        opts, used = [answer_value], {answer_value}
        for offset in offsets:
            candidate = answer_value + offset * max(1, abs(b))
            if candidate not in used and len(opts) < 5:
                used.add(candidate)
                opts.append(candidate)
        index = (variant * 3 + 1) % len(opts)
        opts[0], opts[index] = opts[index], opts[0]
        items.append((f"SER-{name}-{variant}", "SERIES", {"t": shown, "o": opts}, index,
                      difficulty + 0.2 * (variant - 1)))

# ---- Odd one out ------------------------------------------------------------------------------
# Four figures keep a rule and one breaks it; the subtler the attribute, the harder it is to see.
ODD = [
    ("shape", -2.0), ("fill", -1.4), ("count", -0.6), ("rot", 0.4), ("pair", 1.2),
]

for attribute, difficulty in ODD:
    for variant in range(3):
        base = fig(SHAPES[variant % 3], FILLS[variant % 3], 1 + variant % 3, 0, "m")
        figures = [dict(base) for _ in range(5)]
        # The four that keep the rule still differ in something that is not the rule, so the odd
        # one cannot be found by looking for the figure that stands out at all.
        for i, f in enumerate(figures):
            f["r"] = 0 if attribute == "rot" else 45 * (i % 3)
            if attribute != "size":
                f["z"] = SIZES[i % 3]
        odd_index = (variant * 2 + 1) % 5
        odd = figures[odd_index]
        if attribute == "shape":
            odd["s"] = SHAPES[(SHAPES.index(base["s"]) + 3) % len(SHAPES)]
        elif attribute == "fill":
            odd["f"] = FILLS[(FILLS.index(base["f"]) + 1) % len(FILLS)]
        elif attribute == "count":
            odd["n"] = base["n"] + 1
        elif attribute == "rot":
            odd["r"] = 30
        else:
            # Two attributes move together in the four, and apart in the odd one.
            for i, f in enumerate(figures):
                f["n"] = 1 + (i % 2)
                f["f"] = "solid" if f["n"] == 2 else "none"
            odd["f"] = "solid" if odd["n"] == 1 else "none"
        items.append((f"ODD-{attribute}-{variant}", "ODD", {"g": figures}, odd_index,
                      difficulty + 0.2 * (variant - 1)))

# ---- Analogies: A is to B as C is to ? ---------------------------------------------------------
# The transformation has to be read off A -> B and applied to C; more steps, more to read.
ANALOGY = [("single", -1.6, 1), ("double", 0.0, 2), ("triple", 1.2, 3)]


def transform(f, steps, seed):
    out = dict(f)
    if steps >= 1:
        out["n"] = 1 + (f["n"] % 3)
    if steps >= 2:
        out["f"] = FILLS[(FILLS.index(f["f"]) + 1) % len(FILLS)]
    if steps >= 3:
        out["r"] = (f["r"] + 45) % 180
        out["z"] = SIZES[(SIZES.index(f["z"]) + 1 + seed) % len(SIZES)]
    return out


for name, difficulty, steps in ANALOGY:
    for variant in range(4):
        a = fig(SHAPES[variant % len(SHAPES)], FILLS[variant % 3], 1 + variant % 3,
                45 * (variant % 2), SIZES[variant % 3])
        b = transform(a, steps, variant)
        c = fig(SHAPES[(variant + 2) % len(SHAPES)], FILLS[(variant + 1) % 3],
                1 + (variant + 1) % 3, 45 * ((variant + 1) % 2), SIZES[(variant + 2) % 3])
        answer = transform(c, steps, variant)
        opts, index = options_for(answer, 5, variant * 5 + steps)
        items.append((f"ANL-{name}-{variant}", "ANALOGY", {"a": a, "b": b, "c": c, "o": opts},
                      index, difficulty + 0.15 * (variant - 1.5)))

# ---- The migration ----------------------------------------------------------------------------
lines = ["-- // add_iq_items",
         "-- The IQ item bank: matrices, number series, odd-one-out and analogies, generated from",
         "-- templates so the bank is the same everywhere it is installed. DIFFICULTY is in Rasch",
         "-- logits, set from what each template's rules are worth and recalibrated from real",
         "-- answers once an item has been seen enough times (IqService.recalibrate).",
         ""]

for code, kind, payload, answer, difficulty in items:
    body = json.dumps(payload, separators=(",", ":"))
    assert "'" not in body, code
    assert len(body) < 3800, (code, len(body))
    lines.append("INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)")
    lines.append(f"    VALUES (iq_item_seq.NEXTVAL, '{code}', '{kind}', '{body}', {answer}, {difficulty:.2f}, SYSDATE)")
    lines.append("/execute/")
    lines.append("")

lines += ["-- //@UNDO", "DELETE FROM Q_IQ_ITEM", "/execute/", ""]

out = "\n".join(lines)
path = ("C:/Users/vanyo/IdeaProjects/PlayQuiz-BE/persistence/src/main/resources/migrations/scripts/"
        "20260922140200_add_iq_items.sql")
with open(path, "w", encoding="utf-8") as handle:
    handle.write(out)

print(f"{len(items)} items")
for kind in ("MATRIX", "SERIES", "ODD", "ANALOGY"):
    group = [i for i in items if i[1] == kind]
    print(f"  {kind}: {len(group)}  difficulty {min(i[4] for i in group):.2f} .. {max(i[4] for i in group):.2f}")
print("longest payload", max(len(json.dumps(i[2], separators=(',', ':'))) for i in items))
