def lng(num):
    result=0
    for i in range(1,64):
        if bt(num,i):
            result+=1
    return result

def bt(num, i):
    return 1L<<(i-1) & long(num)

def frst(num):
    for i in range(1,64):
        if bt(num,i): return num ^ (1L << (i-1))

def scnd(num):
    for i in range(64,1,-1):
        if bt(num,i): return num ^ (1L << (i-1))

def atomic(num):
    for i in range(1,64):
        if bt(num,i): return i

def orgNames(num):
    result=[]
    for i in range(1,64):
        if bt(num,i):result.append(i)
    return result


if __name__ == '__main__':
    lng(4)
    
    
