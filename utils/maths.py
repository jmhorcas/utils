def int2sci(n: int, precision: int = 2) -> str:
  """Convert a large integer into scientific notation.
  
  This handles very large integers that Python cannot convert to float, 
  avoiding the `OverflowError: int too large to convert to float`.
  """
  if n == 0:
    return '0e0'
  exponent = len(str(abs(n))) - 1
  mantissa = n / (10 ** exponent)
  return f'{mantissa:.{precision}f}e{exponent}'
