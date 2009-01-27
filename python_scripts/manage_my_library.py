from os.path import exists, isfile
from shutil import move
from os import listdir, sep
import xml.dom.minidom
from copy import copy


import xml.dom.minidom

#use xpdf tool t
#pdftotext.exe -l 50 infile outfile
#regex to extract isbn-10 "(?:[\d]-?){9}[\dxX]"
#src_dir=u'g:/library/arabic'
#dist_dir=u'arabic'



def test_isbn():
    out = open('out_isbns.txt', 'w')
    for line in open('out_file.txt'):
        a = line.split(' ')
        out.write('\n' + a[0])
    
    out.close()

global xpdf
xpdf='d:/eclipse/windows/workspace/python/xpdf/pdftotext.exe'

def get_isbn(txt=None):
    import re
    isbn=re.compile("(?:[\d]-?){9}[\dxX]")
    matches = isbn.findall(txt)
    result=set()
    if not matches: return []
    for i in matches:
        i=i.replace('-','')
        if validata_isbn(i):
            result.add(i)
    return list(result)

def validata_isbn(isbn=None):
    """True if (1*i1+ 2*i2+ + )% 11 =0"""
    isbn=isbn.strip().replace('-', '').lower().replace('x','')
    digi=[ int(isbn[j])* (j+1) for j in range(len(isbn))]
    
    #check  111111111111 9999999999
    digi_m=[digi[0]==i for i in digi]
    if all(digi_m):return False
    #check mod 11
    sum=0
    for item in digi:
        sum=sum+item
    if sum%11 == 0:return True
    else:
        print isbn
        return False

def cp_files_in_tree(src_dir=None, dist_dir=None, current=None):
    from os import sep, listdir, mkdir
    from shutil import copyfile, move
    from os.path import isdir,exists, isfile
    is_file=isfile(src_dir+sep+current)
    exsts=exists(dist_dir+sep+current)
    
    if is_file:
        if not exsts:
            copyfile(src_dir+sep+current, dist_dir+sep+current)
            print 'cp ', src_dir+sep+current
        return
        
    if not exsts:
        mkdir(dist_dir+sep+current)
        print dist_dir+sep+current
        return
    #this is a dir and is exists
    lst=listdir(unicode(src_dir+sep+current))
    for i in lst:
        cp_files_in_tree(src_dir, dist_dir,current+sep+i)
        

def fn_with_files(fn=None, src_dir=None, dist_dir=None, current=None, fileName=None, file_type=None):
    from os import sep, listdir
    from shutil import copyfile, move
    from os.path import isdir,exists, isfile
    if isfile(src_dir+sep+current):
        if not exists(dist_dir+sep+current) and current.lower().endswith(file_type):
            fn(src_dir+sep+current, dist_dir+sep+fileName)
        return
    lst=listdir(unicode(src_dir+sep+current))
    for i in lst:
        fn_with_files(fn, src_dir, dist_dir, current+sep+i, i, file_type)
        

def copy_or_move(fn=None, src_dir=None, dist_dir=None, file_type=None):
    """copy or move all doc.s of type to one dist_dir """
    from os import listdir
    from shutil import copyfile, move
    lst=listdir(src_dir)
    for file in lst:
        fn_with_files(fn, src_dir, dist_dir, file, file, file_type)
      

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


def to_txt_file(xpdf=None,src_file=None, dist_file=None):
    """ pdftotext.exe -l lastPage src_file dist_file"""
    import os
    if xpdf == None: return
    from os.path import exists
    from os import sep
    exe=xpdf+' -l 20 '+src_file+' '+dist_file
    if(exists(xpdf) and exists(src_file)):
        print exe
        os.system(exe)
    else:
        print 'error in :'+ exe

def get_isbn_from_file(file_name=None):
    isbn=get_isbn(file_name)
    if len(isbn)==0:
        global xpdf
        to_txt_file(xpdf,file_name, '1.txt')
        isbn=get_isbn(open('1.txt').read())

    if len(isbn)>0:
         return isbn[0]
    else:
        print 'error in isbn',isbn ,' for file ', file_name
        return 0
          
def rename_with_isbn(src_dir=None, dist_dir=None, file_type=None):
    from os import sep, listdir
    from shutil import move,copyfile
    
    a=listdir(src_dir)
    for i in a:
        if not i.lower().endswith(file_type):continue
        in_file=src_dir+ sep+i
        isbn=get_isbn_from_file(in_file)
        if isbn:
            copyfile(in_file, out_file)
        else:
            print 'error in isbn',isbn ,' for file ', i



def dele_spaces_from_filenames(src_dir=None, dist_dir=None):
    from os import listdir,sep
    from shutil import move
    files=listdir(src_dir)
    for fname in files:
        name=fname.replace(' ','_')
        name=name.replace('\t','_')
        name=name.replace('&','and')
        move(src_dir+sep+fname,src_dir+sep+name)

def list_isbn_in_dir(src_dir=None):
    from os import listdir,sep
    from shutil import move
    files=listdir(src_dir)
    for fname in files:
        name=fname.replace('.pdf','')
        print name

def generate_isbn_links(src_dir=None, out_file=None):
    from os import listdir,sep
    out=open(out_file,'w')
    files=listdir(src_dir)
    for fname in files:
        isbn=get_isbn_from_file(src_dir+sep+fname)
        if isbn:
            line=isbn+' '+fname
            out.write('\n'+line)
            print line
    out.close()

def get_isbns_in_dir(src_dir=None, file_type='.pdf'):
    from os import listdir, sep
    result=[]
    files=listdir(src_dir)
    
    for fname in files:
        if not fname.lower().endswith(file_type):continue
        isbn=get_isbn_from_file(src_dir+sep+fname)
        if isbn:
            print 'add to list ', isbn
            result.append((isbn,fname))
    return result

#if __name__ == "__main__":
    #print getISBN('  1863-1901. 2006. ISBN-13: 978-90-04-15185-7, ISBN-10: 90-04-15185-0  ISBN: 0-596-00183-5')
    #cp_only_files(src_dir,dist_dir)
    #src_dir='data'
    #dist_dir='isbn'
    
    #dele_spaces_from_filenames(src_dir)
    #rename_with_isbn('data','isbn','.pdf')
    #list_isbn_in_dir('isbn')
    #generate_isbn_links('G:\\librarynew\\pdf', 'out_file.txt')
    #cp_files_in_tree(u'D:/library', u'G:/library','')
    #rename_all_tree()
    #test_isbn()
        
    #print 'end'

class Book:
    import xml.dom.minidom
    from os.path import exists, isfile
    from shutil import copyfile, move

    

    def __init__(self,book_node=None):
        self.__s='~~'
        if book_node == None: return
        self.handleBook(book_node)
        
    def handleLinks(self,book_node=None):
        """
        <Links>
        <Link ID="1">
        <Title>title</Title>
        <Path>D:\hi..JPG</Path>
        </Link>
        <Link ID="2">
        <Title>title2</Title>
        <Path>D:\a.xml</Path>
        </Link>
        """
        self.links=[]
        links_node=book_node.getElementsByTagName('Links')
        if not links_node:return
        links_node=links_node[0]
        links=links_node.getElementsByTagName('Link')
        if not links:
            self.links=[]
            return
        for link in links:
            lnk_title=self.get_value(link,'Title')
            lnk_path=self.get_value(link,'Path')
            self.links.append((lnk_title,lnk_path))

    def handleBook(self,book_node=None):
        """handle a book node """
        self.title=self.for_file_name(self.get_value(book_node,'Title'))
        self.author=self.for_file_name(self.get_value(book_node,'Author'))
        self.edition=self.for_file_name(self.get_value(book_node,'Edition'))
        self.publisher=self.for_file_name(self.get_value(book_node,'Publisher'))
        self.isbn=self.for_file_name(self.get_value(book_node,'ISBN'))
        self.img=self.get_value(book_node,'FrontCover')
        self.imgb=self.get_value(book_node,'BackCover')
        self.format=self.for_file_name(self.get_value(book_node, 'Format').lower())
        self.handleLinks(book_node)
        #self.links=self.get_value(book_node,'Links')
        #return (title, author, edition, publisher, isbn, img)
    
    def for_file_name(self, text):
        text=text.replace('\t', ' ')
        text=text.replace('\n', ' ')
        text=text.replace(':', ' ')
        text=text.replace('/', ' ')
        text=text.replace('  ', ' ')
        return text
    
    def get_value(self,book_node=None,node_name=None):
        """ Get the text value of the tag"""
        node = book_node.getElementsByTagName(node_name)
        if not node:
            return ''
        node=node[0] 
        text= " ".join(t.nodeValue for t in node.childNodes if t.nodeType == t.TEXT_NODE)
        return text
    
    def add_link(self, file_name=None):
        if not exit(filename) or not isfile(filename):
            return
        self.links.append('file:',file_name)
    
    def to_file_name(self):
        return self.title+self.__s+self.author+self.__s+self.publisher +self.__s+self.isbn
    
    def rename_file(self, src_dir=None, file_name=None, file_type='.pdf'):
        from os.path import isdir,exists, isfile
        from os import rename, mkdir
        from shutil import copyfile, move

        print file_name
        if not file_name.lower().strip().endswith(file_type):
            return        
        if not exists(src_dir+sep+file_name) or not isfile(src_dir+sep+file_name):
            return
        new_file_name=self.to_file_name()+file_type
        new_file_name=new_file_name.replace(',','-')
        new_file_name=new_file_name.replace('\t',' ')
        new_file_name=new_file_name.replace('\n',' ')
        new_file_name=new_file_name.replace(':',' ')
        new_file_name=new_file_name.replace('/',' ')

        book_dir=src_dir #+sep+self.to_file_name()
        #try:
        rename(src_dir+sep+file_name, book_dir+sep+self.to_file_name()+file_type)
        #except : print 'execpt rename  file' 

        try:
            #mkdir(book_dir)
            if self.img:
                copyfile(self.img, book_dir+sep+self.to_file_name()+'.jpg')
            if self.imgb:
                copyfile(self.imgb, book_dir+sep+self.to_file_name()+'_b.jpg')
        except : print 'execption in img' 
            

def handleELibPro(filename=None):
    """ return list of Book class objects in the xml filename
        (title, author, edition, publisher, isbn, img)
    """
    if filename==None or not exists(filename) or not isfile(filename):
        return []
    
    result=[]
    xmldoc=xml.dom.minidom.parse(filename)
    
    eLibPro=xmldoc.getElementsByTagName('eLibPro')[0]
    if not eLibPro: return result
    books=eLibPro.getElementsByTagName('Book')
    for book in books:
        result.append(Book(book))
    return result


src_dir='data'
ebook_list= handleELibPro('elibpro.xml')
book_map={}
for book in ebook_list:
    book_map[book.isbn]=book
    

isbn_list=get_isbns_in_dir(src_dir)
print isbn_list

for isbn,fileName in isbn_list:
    try:
        if not isbn in book_map:continue
        print 'rename ', isbn, fileName
        book.rename_file(src_dir, fileName, '.pdf')
    except : print 'error'
