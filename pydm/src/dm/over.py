class c:
    def __add__(o1,o2):
        result=c()
        result.val=o1.val+2*o2.val
        return result


a=c()
a.val=2
b=c()
b.val=5


