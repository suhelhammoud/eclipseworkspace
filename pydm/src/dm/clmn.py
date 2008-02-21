import cnm
from copy import deepcopy


class Column:
    
    def __init__(self,id,clmns=None,data=None):
        """init the Column"""

        self.id=id
        self.id1=None
        self.id2=None
        self.items=None
        self.supp=0
        self.conf=0
        self.att=dict()
        self.data=data
        self.columns= [columns,clmns][bool(clmns)]

        if data != None:
            cols=cnm.orgNames(id)
            for i in cols:
                self.att[i-1]=deepcopy(data.att[i-1])
        
    def length(self):
        return len(self.items)

    def addlines(self,v=None):
        """ v=2 or v=[2,3,4]"""
        lst=[]
        if isinstance(v, int): lst=[v]
        else: lst=v
        
        for i in lst:
            if i not in self.items:
                pass

        pass

    def additem(self,v=None):
        pass        
        
    def setSubColumns(self):
        if self.columns==None:return False
        if self.isAtomic():return False
        self.id1=cnm.frst(self.id)
        self.id2=cnm.frst(self.id)
        
        if self.id1 not in self.columns or self.id2 not in self.columns: return False
        else: return True

            
    def isAtomic(self):
        if cnm.lng(self._id)==1 : return True
        else: return False
        
    def generateAtomicOccs(self):
        result=None
        if not self.isAtomic(self): return
        
    
    
         
        
        
