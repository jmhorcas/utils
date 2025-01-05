import os


def int2sci(number: int, precision: int = 2) -> str:
    """Convert a large integer into scientific notation.
    
    This handles very large integers that Python cannot convert to float, 
    avoiding the `OverflowError: int too large to convert to float`.
    """
    if number == 0:
        return '0e0'
    exponent = len(str(abs(number))) - 1
    mantissa = number / (10 ** exponent)
    return f'{mantissa:.{precision}f}e{exponent}'


def float2exp(number: float, precision: int = 2) -> str:
    """Rounds a float to the specified number of decimal places. 
    If the result is 0.0, it returns the number in exponential format.
    """
    rounded = round(number, precision)
    if rounded == 0.0:
        return f'{number:.{precision}e}'
    return str(rounded)


def get_filepaths(dir: str, extensions_filter: list[str] = []) -> list[str]:
    """Get all filepaths of files with the given extensions from the given directory."""
    filepaths = []
    for root, dirs, files in os.walk(dir):
        for file in files:
            if not extensions_filter or any(file.endswith(ext) for ext in extensions_filter):
                filepath = os.path.join(root, file)
                filepaths.append(filepath)
    return filepaths
