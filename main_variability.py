import sys
import inspect
import pkgutil
import logging
import time

def main() -> None:
    stdlib_module_names = sys.stdlib_module_names
    print(f'Standard library modules: {len(stdlib_module_names)}')
    builtin_module_names = sys.builtin_module_names
    print(f'Modules compiled in this Python interpreter: {len(builtin_module_names)}')

    members = inspect.getmembers(time)
    print(f'Members: {len(members)}')
    for m in members:
        print(f'|-{m} ({type(m)})')
    modules = [name for name, obj in members if inspect.ismodule(obj)]
    classes = [name for name, obj in members if inspect.isclass(obj)]
    methods = [name for name, obj in members if inspect.ismethod(obj)]
    functions = [name for name, obj in members if inspect.isfunction(obj)]
    
    print(f'Modules:')
    for cls in modules:
        print(f'|-Module: {cls}')

    print(f'Classes:')
    for cls in classes:
        print(f'|-Class: {cls}')

    print(f'Methods:')
    for cls in methods:
        print(f'|-Method: {cls}')

    print(f'Functions:')
    for cls in functions:
        print(f'|-: {cls}')

    raise Exception
    #attributes = [name for name, _ in inspect.getmembers(cls) if not name.startswith("__")]
    attributes = [name for name, obj in inspect.getmembers(cls) if not name.startswith("__")]
    methods = [name for name, obj in inspect.getmembers(cls, predicate=inspect.isfunction)]
    for m in methods:
        print(f'Method: {m}')
    for a in attributes:
        print(f'Attribute: {a} {type(a)}')
        raise Exception


    
    # modules = list(pkgutil.iter_modules())
    # print(f'#Modules: {len(modules)}')

    # print(f'#Modules: {len(list(sys.modules))}')

if __name__ == '__main__':
    main()