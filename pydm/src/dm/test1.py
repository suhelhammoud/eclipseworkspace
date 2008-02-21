







filename="F:\\eclipse\\eclipse.3.3\\workspace\\jython\src\dm\\116.arff"
    
    
from dm import Data
from clmn import Column


    
d=Data()
d1=Data()
columns=dict()
d.readData(filename)
col=Column(1,columns,d)
d1.addline('2,2,2')
  
d2=d+d1  
   
