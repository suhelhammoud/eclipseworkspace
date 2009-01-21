#use xpdf tool t
#pdftotext.exe -l 50 infile outfile
#regex to extract isbn-10 "(?:[\d]-?){9}[\dxX]"
src_dir=u'g:/library/arabic'
dist_dir=u'arabic'



def copy_files(src_dir=None, dist_dir=None, current=None, fileName=None):
    from os import listdir, sep
    import shutil
    from os.path import isdir, exists, isfile
    if isfile(src_dir+sep+current):
        if not exists(dist_dir+sep+current) and current.lower().endswith(u'.pdf'):
            shutil.copyfile(src_dir+sep+current, dist_dir+sep+fileName)
        else:
            pass
            #print 'duplicate file or not pdf', unicode(src_dir+sep+current)
        return
    
    lst=listdir(unicode(src_dir+sep+current))
    for i in lst:
        copy_files(src_dir, dist_dir, current+sep+i, i)
        
def cp_only_files(src_dir=None, dist_dir=None):
    from os import listdir
    lst=listdir(src_dir)
    for i in lst:
        copy_files(src_dir, dist_dir, i, i)
        

def copy_only_dir(src_dir=None, dist_dir=None, current=None):
    from os import mkdir, listdir, sep
    from os.path import isdir, exists
    
    if not isdir(src_dir+sep+current):
        return
    else:
        if not exists(dist_dir+sep+current):
            mkdir(dist_dir+sep+current)
            print "cp dir:", src_dir+sep+current
        
    lst=listdir(src_dir+sep+current)
    for i in lst:
        copy_only_dir(src_dir, dist_dir, current+sep+i)

            
def cp_only_dirs(src_dir=None, dist_dir=None):
    from os import listdir
    lst=listdir(src_dir)
    for i in lst:
        copy_only_dir(src_dir, dist_dir, i)
    
#cp_only_dirs(src_dir, dist_dir)


def to_txt_file(src_file=None, dist_file=None):
    import os, shutil
    from os import sep
    os.system('d:/eclipse/windows/workspace/python/xpdf/pdftotext.exe -l 50 '
                  + src_file+' '+dist_file)
    
def rename_with_isbn(src_dir=None):
    import os, shutil
    from os import sep
    a=os.listdir(src_dir)
    for i in a:
        if not i.lower().endswith('pdf'):continue
        in_file=src_dir+os.sep+i
        to_txt_file(in_file, '1.txt')
        isbns=getISBN(open('1.txt').read())
        out_file=in_file
        for isbn in isbns:
            out_file=out_file+'('+isbn+')'
        shutil.move(in_file,out_file)

def validata_isbn(isbn=None):
    i=isbn.replace('-', '')
    digi=[ int(i[j])* (j+1) for j in range(10)]
    sum=0
    for item in digi:
        sum=sum+item
    if sum%11 == 0:return True
    else:
        print isbn
        return False


def getISBN(txt=None):
    import re
    isbn=re.compile("(?:[\d]-?){9}[\dxX]")
    matches = isbn.findall(txt)
    result=[]
    if not matches: return []
    for i in matches:
        if validata_isbn(i):
            result.append(i)
    return [isbn.replace('-', '') for isbn in result]

    

if __name__ == "__main__":
    #print getISBN('  1863-1901. 2006. ISBN-13: 978-90-04-15185-7, ISBN-10: 90-04-15185-0  ISBN: 0-596-00183-5')
    #cp_only_files(src_dir,dist_dir)
    rename_with_isbn('data')
#    if len(args) < 3:
#        print "add source and distenation folders"
#    else:
#        cp_only_files(src_dir,dist_dir)
