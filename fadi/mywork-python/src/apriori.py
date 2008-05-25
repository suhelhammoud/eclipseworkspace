from util import tic

def tic(): 
    import time
    return time.time()

def toc(t=None): 
    import time
    return time.time()-t


def removeall(path):
    import os
    if not os.path.exists(path):return
    if not os.path.isdir(path):return
    
    files=os.listdir(path)
    print 'delete files', files
    for x in files:
        os.remove(path+ os.sep+x)
    os.rmdir(path)
            
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

def sub_len(s,r,length):
    if length == 0:
        r.append(tuple(s))
    else:
        for item in s:
            subs=s[:]
            subs.remove(item)
            subs.sort()
            if tuple(subs) in r: continue
            r.append(tuple(subs))
            sub_len(subs, r,length-1)

def sub_len_i(s,sub,r,start,length):
    if len(sub)==length:
        r.append(tuple(sub))
    else:
        for i in range(start,len(s)):
            tsub=sub[:]
            tsub.append(s[i])
            sub_len_i(s, tsub, r, i+1, length)
        
def subset_i(lst,length):
    lst=list(lst)
    r=[]
    sub_len_i(lst, [],r ,0,length)
    r.sort()
    return r

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
    """
    return list of sublists by removing one item each time
    """
    r=[]
    for item in lst:
        s=list(lst)
        s.remove(item)
        s.sort()
        r.append(tuple(s))
    return r

def ap_join(Li):
    """
    Li: dict of Large itemset of length i, each item is in tuple {tuple():freq}
    return list of new joined items Ji [ tuple()... tuple()]
    """
    r=[]
    keys=Li.keys()
    N=len(keys)
    for i in range(N-1):
        ii=list(keys[i])
        for j in range(i+1,N):
            jj=list(keys[j])
            s=set( ii+ jj)
            if len(s) != len(ii)+1:continue
            lst=list(s)
            lst.sort()
            if tuple(lst) not in r:
                r.append(tuple(lst))
    return r

def ap_prune(Ji,Li_1):
    """
    check if each intermediate subsets is in Li_1
    Ji: list of items tuple
    Li_1: dict{ tuple:freq}
    return list of candidates Ci
    """
    r=[]
    for item in Ji:
        sub_lsts=sub_remove_one(item)
        isInLi_1=True
        for lst in sub_lsts:
            if lst not in Li_1:
                isInLi_1=False
                break
        if isInLi_1:
            r.append(item)
    return r

def ap_support_count(Ci=None, datafile=None, min_support=None):
    """
    Ci: list of candidate items of lenght i
    datafile: string of data filename
    return dict of Li {(tuple)Ci:freq}
    """
    r=dict()    
    for line in open(datafile):
        ln=line.split()
        s=set(ln)
        for item in Ci:
            subset=set(item)
            #print 'support count',subset, s
            if subset.issubset(s):
                if item in r:
                    r[item]=r[item]+1
                else:
                    r[item]=1
    for k,v in dict(r.items()).items():
        if v < min_support:
            del r[k]
    return r



def confidence(dirIn, dirOut, index, min_confidence):
    """
    """
    import os
    fout=open(dirOut+os.sep+str(index) , 'w')
    files=os.listdir(dirIn)
    files=[int(i) for i in files]
    files.sort()
    files=[str(i) for i in files]
    N= len( files ) 
    L_left=load( dirIn + os.sep + str(index) )
    #print 'L_left=',L_left
    count=0
    for i in range(index,N):
        #L_all=update_L(None, dirIn + os.sep + files[i] )
        L_all=load(dirIn+ os.sep+files[i])
        #print 'L_all',L_all
        for k,v in L_all.items():
            lst=subset_i(k,index)
            #print k,v,lst
            for left in lst:
                right=tuple( set(k).difference(set(left)) )
                v_left=L_left[left]
                c= float(v) / float(v_left) 
                if c > min_confidence:
                    fout.write(str(c) +":" + str(left)+ ":"+ str(right)+ ":"+ str(v_left)+ ":"+ str(v)+"\n")
                    count=count+1
    fout.close()
    return count

def ap_confidence(dirIn, dirOut, min_confidence):
    """
    
    """
    import os
    N= len( os.listdir(dirIn) )
    for i in range(1,N+1):
        confidence(dirIn, dirOut, i, min_confidence)

def dump(var, filename=None):
    """
    Save variable "var" in "filename"
    """
    import pickle
    f=open(filename, 'w')
    pickle.dump(var, f)
    f.close()

def load(filename=None):
    """
    return variable saved in "filename"
    """
    import pickle
    f= open(filename)
    r=pickle.load(f)
    f.close()
    return r

def ap_L1(datafile, min_support_f, data_size):
    """
    Get distinct items from "datafile"
    return: L1
    """
    r={}
    sz=0
    for line in open(datafile, 'r'):
        sz=sz+1
        item=line.split()
        for i in item:
            t=tuple(i)
            if t in r:
                r[t]=r[t]+1
            else:
                r[t]=1
    
    min_support=min_support_f * sz
    data_size.append(sz)
    print 'Size=',sz, ' data_size=',data_size    
    for k,freq in dict(r).items():
        if freq < min_support:
            del r[k]
    return r

def sort(dirName, outfile):
    """
    read all the rules files in "dirName", sort it in reverse
    write the result in "outfile"
    """
    import os
    files=os.listdir(dirName)
    r={}
    delta=0.0000000001
    shift=0.0
    #count=0
    for f in files:
        fname=dirName + os.sep + f
        for line in open(fname):
            a=line.split(':')
            r[float(a[0])+shift]=line
            shift=shift+delta
            #count=count+1
    srt=r.keys()
    srt.sort(reverse=True)
    fout=open(outfile,'w')
    for i in srt:
        fout.write(r[i])
    #print 'rules sorted', len(srt), count
    return len(srt)
    
def apriori(datafile, min_support_f, min_confidence, resultfile='result.txt'):
    import os
    t_start=tic()
    #create dir_L directory
    dir_L='l'
    if os.path.exists(dir_L):removeall(dir_L)
    os.mkdir(dir_L)

    t=tic()
    data_size=[]
    L=ap_L1(datafile, min_support_f, data_size)
    print 'data_size',data_size 
    min_support=min_support_f * data_size[0]
    print 'total data size :',data_size[0], ' , minimum support:',min_support
    
    print 'L1: ',len(L),' time:',toc(t),' form start:', toc(t_start)
    
    dump(L, dir_L+os.sep+"1")
    
    for i in range(2,20):
        t=tic()
        L=load(dir_L+os.sep+str(i-1))
        J=ap_join(L)
        C=ap_prune(J, L)
        L_plus=ap_support_count(C, datafile, min_support)
        print 'L',i,': ',len(L_plus),' time:',toc(t),' form start:', toc(t_start)
        if len(L_plus)== 0: break
        dump(L_plus, dir_L+os.sep+str(i))
        L=L_plus

    #calculate confidence
    dir_C='c'
    if os.path.exists(dir_C): removeall(dir_C)
    os.mkdir(dir_C)
    N=len(os.listdir(dir_L))
    print 'Number of files in ',dir_L, ' is ', N
    
    count=0
    for i in range(1,N):
        t=tic()
        count_conf=confidence(dir_L, dir_C, i, min_confidence)
        count=count+count_conf
        print 'Time to calc confidence ',i,' is ', toc(t), ' from start ', toc(t_start) 
    
    print 'total rules', count
    print 'Start sorting the result'
    t=tic()
    num_rules = sort(dir_C, resultfile)
    print 'Num of rules sorted',num_rules,' finish sorting rules at ', toc(t)
    print 'total time elapsed :', toc(t_start)

