'''
id:1238019918
from:eepgssh@gmail.com
fromPage:1
toPage:30
subject:ebook 2-1-30
date:Wed, 25 Mar 2009 21:58:30 +0000
fileName:ebook (9).pdc
'''

from os import listdir,sep
from os.path import isdir,isfile,exists
from dircache import listdir
import urllib2,urllib
from time import time, sleep



base_dir='D:\\eclipse\\windows\\workspace\\pdctools'

def info_file(file):
    result={}
    for i in open(file):
        entry=i.split(":")
        if len(entry)>=2:
            result[entry[0].strip()]=":".join(entry[1:]).strip()
    return result
            
def info_dir(dir_name):
    info= info_file(dir_name+sep+"info.txt")
#    result=[]
#    if "id" in info: result.append(info)
    result=info["id"]+"\t"+info["from"]+"\t"+ info["subject"]+"\t"+info["date"]
    return result

def info_group(dir_name):
    result=[]
    for file in listdir(dir_name):
        result.append(info_dir(dir_name+sep+file))
    return result

    
def info_all(base_dir):
    result=      ["\n--------------------------------in queue-----------------------------------------------------------\n"]
    result.append("\n".join(info_group(base_dir+sep+"download")))

    result.append("\n--------------------------------scanning-----------------------------------------------------------\n")
    result.append("\n".join(info_group(base_dir+sep+"scanning")))

    result.append("\n--------------------------------Scanned------------------------------------------------------------\n")
    result.append("\n".join(info_group(base_dir+sep+"scanned")))

    result.append("\n--------------------------------Zipping------------------------------------------------------------\n")
    result.append("\n".join(info_group(base_dir+sep+"zipping")))

    result.append("\n--------------------------------Zipped-------------------------------------------------------------\n")
    result.append("\n".join(info_group(base_dir+sep+"zipped")))

    result.append("\n--------------------------------Emailing-----------------------------------------------------------\n")
    result.append("\n".join(info_group(base_dir+sep+"emailing")))

    result.append("\n--------------------------------Finished-----------------------------------------------------------\n")
    result.append("\n".join(info_group(base_dir+sep+"emailed")))
    
    return "\n".join(result)

 
#print info_all(base_dir)
import time
while True:
    sleep(6)
    print 'info ',time.time()

    try:
        #url = 'http://localhost:8080/submit'
        url = 'http://nadyelfikr-net.appspot.com/submit'
        info=info_all(base_dir)
        #print info
        values = {'msg' : info}
        data = urllib.urlencode(values)
        req = urllib2.Request(url, data)
        response = urllib2.urlopen(req)
        the_page = response.read()
        print the_page
    
    except :
        print 'error'
    

