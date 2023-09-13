Methodology:
Assign each individual card a unique prime (of the first 52 primes). Use poker hand equivalency classes courtosy of http://www.suffe.cool/poker/7462.html to generate all possible 5 card combinations and assign each a unique prime product. Create a map from prime product to rank and load this when the evaluator launches.

Issues with this approach include long load times. Evaluation is fast as Map lookup is constant time.