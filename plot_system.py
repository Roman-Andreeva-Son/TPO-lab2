"""
import csv
import math
import matplotlib.pyplot as plt

x_values = []
y_values = []

with open("build/results/system.csv", encoding="utf-8") as file:
    reader = csv.reader(file, delimiter=";")
    next(reader)

    for row in reader:
        x = float(row[0])
        y_raw = row[1]

        if y_raw in ("NaN", "Infinity", "-Infinity"):
            continue

        y = float(y_raw)

        # чтобы огромные выбросы около разрывов не ломали масштаб графика
        if abs(y) > 100:
            continue

        x_values.append(x)
        y_values.append(y)

plt.figure(figsize=(12, 6))
plt.plot(x_values, y_values)
plt.axhline(0, linewidth=0.8)
plt.axvline(0, linewidth=0.8)
plt.grid(True)

plt.title("Система функций, вариант 9123")
plt.xlabel("x")
plt.ylabel("f(x)")

plt.savefig("system_graph.png", dpi=300, bbox_inches="tight")
plt.show()
"""
