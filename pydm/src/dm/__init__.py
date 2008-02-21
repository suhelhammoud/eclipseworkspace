
columns={}

filename="F:\\eclipse\\eclipse.3.3\\workspace\\jython\src\dm\\116.arff"
    
def t1():
    from dm import Data
    
    d=Data()
    d1=Data()
    
    d.readData(filename)
    d1.addline('2,2,2')

