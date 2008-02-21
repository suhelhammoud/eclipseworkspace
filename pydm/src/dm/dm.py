from copy import deepcopy


class Data:
    """ """

    def __init__(self,d=None):
        if d != None:
            self.relation=d.relation
            self.att= deepcopy(d.att)
        else:
            self.relation=None
            self.att=dict()
        self.items=[]
    def __add__(d1,d2):

        r=Data()
    
        r.relation=d1.relation,d2.relation
        if d1.length()== 0 and d1.length()==0: return r
        if d1.length()== 0 : return Data.copy(d2,r)
        if d2.length()== 0 : return Data.copy(d1,r)


        for v in d1.items:
            lst=()
            for i,val in enumerate(v):
                lst+=(d1.att[i][val],)
            r.addline(lst)

        for v in d2.items:
            lst=()
            for i,val in enumerate(v):
                lst+=(d2.att[i][val],)
            r.addline(lst)
        return r


    def length(self):
        return len(self.items)
    
    def addline(self,lst=None):
        """ lst= '2,2,3' or ['2','2','3'] """
        
        if isinstance(lst,str):
            lst=lst.lower().strip()
            if lst.startswith('@'):return 0
            lst=lst.split(',')
            
        lst=tuple(lst)
        if len(lst) != len(self.att) and len(lst) == 0:
            raise 'ErrorListLength',str(len(lst))

        lrecord=()
        
        for i, val in enumerate(lst):
            if i not in self.att: self.att[i]=[]
            if val not in self.att[i]: self.att[i].append(val)
            ival=self.att[i].index(val)
            lrecord+=(ival,)
        self.items.append(lrecord)
        return self.length()
        
    def readHeader(self,filename):
        attCount=0
      
        for lne in open(filename):
            lne=lne.lower().strip()
            if lne =='':continue
            #if lne.startswith('@'):continue

            if lne.startswith('@relation'):
                self.relation=lne.split()[1]
                continue
            
            if lne.startswith('@attribute'):
                #tl=lne.strip().split()[1]
                tl2=lne[lne.index('{')+1:lne.index('}')]
                self.att[attCount]=tl2.split(',')
                attCount+=1
                continue
            
            if lne.startswith('@data'): break

        
    def readData(self,filename):
        self.readHeader(filename)
        for lne in open(filename):
            self.addline(lne)
        return self.length()

    def copy(self,s=None,d=None):
        d.relation=s.relation
        d.att=s.att[:]
        d.items=s.items[:]
        return d

    def copyAtt(self,s=None,d=None):
        d.relation=s.relation
        d.att=s.att[:]
        return d
        

    
    def getdatas(self,c=None):
        if c==None:
            c=len(self.att)-1
            
        if c not in self.att:
            raise 'ErrorAttNotFound',c
            return
        r=dict()
        print len(self.att)

        for i in self.att[c]:
            d=Data(self)
            del d.att[c]
            r[i]=d
        
        for lne in self.items:
            cval=self.att[c][lne[c]]
            lst=list(lne)
            del lst[c]
            r[cval].items.append(tuple(lst))
        
        return r
    
    def len(self,v):
        cv=self.att[2].index(str(v))
        sum=0
        for i in self.items:
            if i[2]==cv:sum+=1
        return sum

    def data(self,ln=None):
        r=[]
        lst=[]
        if ln==None:
            lst=self.items
        else:
            lst=[self.items[ln]]
            
        for i in lst:
            lne=()
            for j,v in enumerate(i):
                lne+=(self.att[j][v],)
            r.append(lne)
        return r
                
            
