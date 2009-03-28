import email, getpass, imaplib, os
from os.path import join
from time import time, sleep
download_dir = 'download' # directory where to save attachments (default: current)
work_dir ='downloading'
user = 'pdc.to.jpg@googlemail.com' #raw_input("Enter your GMail username:")
pwd = None #getpass.getpass("Enter your password: ")


def get_address(eml=""):
    eml=eml.strip().split("<")
    if len(eml)==1:
        eml=eml[0][1:-1]
    else:
        eml=eml[1][:-1]
    return eml

def get_title_from_to(subject=None):
        subject=subject.strip()
        subject_arg=subject.split("-")
        if len(subject_arg) != 3 :
            print 'subject args less != 3'
            return None
        #check from page to page
        if int(subject_arg[1])< 1 or int(subject_arg[1])> 600 \
            or int(subject_arg[2])< int(subject_arg[1]) or int(subject_arg[2])> 600:
            print 'from page to page ranges is not ok'
            return None
        return subject_arg
        
    
def login_search_download(criteria="UNSEEN"):
    """return the mails id """
    # connecting to the gmail imap server
    m = imaplib.IMAP4_SSL("imap.gmail.com")
    m.login(user,pwd)
    m.select("Inbox") # here you a can choose a mail box like INBOX instead
    # use m.list() to get all the mailboxes

    resp, items = m.search(None,criteria) # you could filter using the IMAP rules here (check http://www.example-code.com/csharp/imap-search-critera.asp)
    items = items[0].split() # getting the mails id

    for emailid in items:
        sleep(10)
        resp, data = m.fetch(emailid, "(RFC822)") # fetching the mail, "`(RFC822)`" means "get the whole stuff", but you can ask for headers only, etc
        email_body=[]
        try:
            email_body = data[0][1] # getting the mail content
        except:
            print 'error in email body '
            return
        
        mail = email.message_from_string(email_body) # parsing the mail content to get a mail object
        #Check if any attachments at all
        if mail.get_content_maintype() != 'multipart':
            print 'no attachment'
            copy_and_delete(m,emailid,"others")
            continue

        line=[]
        id=str(int(time()))
        line.append("id:"+id)
        line.append("from:"+get_address(mail["From"]))
        subject=mail["Subject"].lower().strip()
        tft=get_title_from_to(subject)
        if tft == None:
            print 'error in subject ',subject
            copy_and_delete(m,emailid,"others")
            continue            
        line.extend(["fromPage:"+ tft[1].strip(),"toPage:"+tft[2].strip() ])
        line.append("subject:"+subject)
        line.append("date:"+mail["Date"])
        print line
        
        # we use walk to create a generator so we can iterate on the parts and forget about the recursive headach
        for part in mail.walk():
            # multipart are just containers, so we skip them
            if part.get_content_maintype() == 'multipart':
                continue

            # is this part an attachment ?
            if part.get('Content-Disposition') is None:
                continue

            filename = part.get_filename()
            counter = 1

            # if there is no filename, we create one with a counter to avoid duplicates
            if not filename:
                filename = 'part-%03d%s' % (counter, 'bin')
                counter += 1

            filename=filename.lower().strip()
            if not filename.endswith(".pdc"):
                print 'attachmet does not end with .pdc'
                #copy_and_delete(m,emailid,"others")
                #continue
            
            os.mkdir(join(work_dir,id))
            att_path = os.path.join(work_dir,id, "ebook.pdc")
            line.append ("fileName:"+filename)
            #Check if its already there
            if not os.path.isfile(att_path) :
                # finally write the stuff
                fp = open(att_path, 'wb')
                fp.write(part.get_payload(decode=True))
                fp.close()
        
        
        copy_and_delete(m,emailid,"pdc")

        #save info.txt
        info_path=os.path.join(work_dir,id, "info.txt")
        f_info=open(info_path,"wb")
        f_info.write("\n".join(line))
        f_info.close()
        
        os.rename(join(work_dir,id),join(download_dir,id))
        #append to queue.txt
        f=open('control/download.txt','a')
        f.write("~".join(line)+"\n")
        f.close()
        
        #exit loop after one iteration because of unknown error at email_body = data[0][1]
        break
    
    
    m.close()
    m.logout()

def copy_and_delete(m=None,emailid=None,dist_folder=None):
    print 'copy email to ', dist_folder
    m.copy(emailid, dist_folder)
    sleep(10)
    m.store(emailid, "+FLAGS.SILENT", '(\\Deleted)')
                    
def main():
    global pwd
    if pwd==None:
        pwd=raw_input("what is the password for "+user+" :")

    print "Monitoring the Mail server  "
    print 'login, search and downlaod '
    login_search_download("UNSEEN")

if __name__ == '__main__':
    while True:
        try:
            main()
            sleep(20)
        except Exception(e,msg):
            print e,msg
