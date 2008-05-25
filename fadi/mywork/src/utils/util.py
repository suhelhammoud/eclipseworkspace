from copy import deepcopy


def addLineToFile(src=None,dis=None):
    fout=open(dis,"w")
    fin=open(src)
    counter=1
    for line in fin:
        line=str(counter)+":"+line
        fout.write(line)
        counter=counter+1
    fin.close()
    fout.close()
        
def sample():
    r=[
       [1,2,5],
       [2,4],
       [2,3],
       [1,2,4],
       [1,3],
       [2,3],
       [1,3],
       [1,2,3,5],
       [1,2,3]
       ]
    return r

def conver_file_format(filename="../data/retail.dat"):
    f1=open(filename+"2","w")
    for line in open(filename):
        ln=",".join(line.strip().split(" "))+"\n"
        f1.write(ln)
    f1.close()

def check_duplicates(filename=None):
    """ print the line number----> duplicate item """
    i=0
    for line in open(filename):
        a=line.split(" ")
        i=i+1
        b=set(a[:])
        for item in b:
            if a.count(item) > 1:
                print "line",i,"---->",item
    print "end"


#tests
#check_duplicates("../data/retail.dat")

def subsets(s,r):
    if len(s)== 1:
        pass
    else:
        for item in s:
            subs=s[:]
            subs.remove(item)
            subs.sort()
            if tuple(subs) in r: continue
            r.append(tuple(subs))
            subsets(subs, r)

def subs(lst):
    lst=list(lst)
    r=subset_1(lst)
    lst.sort()
    r.append(tuple(lst))
    return r

def subset_1(lst):
    lst=list(lst)
    r=[]
    subsets(lst, r)
    r.sort()
    return r

def sub_remove_one(lst):
    r=[]
    for item in lst:
        s=lst[:]
        s.remove(item)
        s.sort()
        r.append(tuple(s))
    return r

def get_data(filename="../data/d.txt"):
    a=[]
    
    for line in open(filename):
        items=line.split(",")
        a.append(items)
    print "finishd reading file", filename
    return a

def fill_data(data):
    d={}
    
    for row in data:
        r=subs(row)
        for item in r:
            if item in d:
                d[item]=d[item]+1
            else:
                d[item]=1
    dct={}
    dct[0]=d
    return dct

def fill_k(dct):
    d=dct[0]
    r=dct
    for item in d:
        num=len(item)
        if num in r:
            if item in r[num]:continue
            r[num].append(item)
        else:
            r[num]=[item]
    
    return r

        
def add_item(dct,item):
    """ added to the key dict"""
    if item not in dct[0]:
        print "itemNotFound ",str(item)
        return False

    num=len(item)
    if num in dct:
        if item in dct[num]:
            return False
        else:
            dct[num].append(item)
            return True
    else:
        dct[num]=[item]
        return True

def del_item(dct,item):
    if item not in dct[0]:
        return False
    num = len(item)

    if num in dct:
        if item in dct[num]:
            dct[num].remove(item)
            return True
    else:
        return False
    
def in_item(dct,item):
    num = len(item)
    if num in dct:
        if item in dct[num]:
            return True
    else:
        return False



def support(dct,item):
    num=len(item)
    if item in dct[0]:
        return dct[0][item]
    else:
        raise "itemNotFound",item

def confidence(dct,i1,i2):
    lst=list(i1)
    lst.extend(list(i2))
    #print lst
    lst.sort()
    c1=support(dct, tuple(lst))
    c2=support(dct, i1)
    #print c1,c2
    return float(c1)/float(c2)

def gen_freq(dct,supp):
    import copy
    r=copy.deepcopy(dct)
    #r=dct.copy()
    for k,nv in dct.items():
        for item in nv:
            if support(dct,item)< supp:
                del_item(r,item)
        if len(r[k])==0:
            del r[k]
    return r

def gen_candidate(dct):
    import copy
    r=copy.deepcopy(dct)
    for n in range(2,len(dct)):
        #iterate in row
        for k in dct[n]:
            sub=sub_remove_one(list(k))
            #iterate in subsets
            for item in sub:
                #print item
                if not in_item(dct, item):
                    del_item(r,k)
                    print "dl_item ",str(item)
                    break
        if len(r[n])== 0:
            #delete crules up to len(r)+1
            for j in range(n,len(dct)):
                del r[j]
            break
        
    return r


#confidence(dct, (5,), (1,2,))
def gen_confidence(dct,conf):
    print "generate confideces"
    d=dct[0]
    for i in range(2,len(dct)):
        r=dct[i]
        for j in r:
            print j
            j_left=subset_1(j)
            j_right=[]
            for item in j_left:
                item=list(set(j).difference(set(item)))
                item.sort()
                j_right.append( tuple(item))

            for index in range(len(j_left)):
                c=confidence(dct, j_left[index], j_right[index])
                #print str(j_left[index]),"---->",str(j_right[index])," ,c=", str(c)
                if c>=conf:
                    print str(j_left[index]),"---->",str(j_right[index])," :", support(dct, j_left[index])," ,c=", str(c)
            

                               
    

        

def p(dct,s=None):
    if s != None:
        print "----------------",s,"----------------"
    for k,v in dct.items():
        if k==0:continue
        print k
        print v

if __name__ == '__main__':
                
    #data=sample()
    data=get_data()
    dct=fill_data(data)
    dct=fill_k(dct)
    #p(dct,"dct")
    
    dct=gen_freq(dct, 3)
    #p(dct,"gen freq")
    
    #item=(1,5)
    #del_item(dct,item)
    #p(dct,"del "+str(item))
    
    #dct=gen_freq(dct, 2)
    #p(dct,"gen freq")
    
    
    dct=gen_candidate(dct)
    #p(dct,"gen candidate")
    
    gen_confidence(dct, 0.90)
    p(dct,"gen confidence")
