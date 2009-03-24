import email, getpass, imaplib, os
from time import time, sleep
detach_dir = 'download' # directory where to save attachments (default: current)
user = 'pdc.to.jpg@googlemail.com' #raw_input("Enter your GMail username:")
pwd = 'yasintaha' #getpass.getpass("Enter your password: ")


def get_address(eml=""):
    eml=eml.strip().split("<")[1]
    eml=eml[:-1]
    return eml
    
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
        resp, data = m.fetch(emailid, "(RFC822)") # fetching the mail, "`(RFC822)`" means "get the whole stuff", but you can ask for headers only, etc
        email_body = data[0][1] # getting the mail content
        mail = email.message_from_string(email_body) # parsing the mail content to get a mail object

        #Check if any attachments at all
        if mail.get_content_maintype() != 'multipart':
            print 'no attachment'
            copy_and_delete(m,emailid,"others")
            continue


        print "["+mail["From"]+"] :" + mail["Subject"]
        line=[]
        id=str(int(time()))
        line.append("id:"+id)
        line.append("from:"+get_address(mail["From"]))
        
        subject=mail["Subject"].lower().strip()
        if not subject.startswith("scan"):
            print 'not starts with scan'
            copy_and_delete(m,emailid,"others")
            continue
        subject_arg=subject.split("-")
        if len(subject_arg) != 3 :
            print 'subject args less != 3'
            copy_and_delete(m,emailid,"others")
            continue
        line.extend(["fromPage:"+ subject_arg[1],"toPage:"+subject_arg[2] ])
        line.append("subject:"+subject)
        line.append("date:"+mail["Date"])

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
                copy_and_delete(m,emailid,"others")
                continue
            
            save_dir=os.path.join(detach_dir,id)
            os.mkdir(save_dir)
            att_path = os.path.join(save_dir, "ebook.pdc")
            line.append ("fileName:"+filename)
            #Check if its already there
            if not os.path.isfile(att_path) :
                # finally write the stuff
                fp = open(att_path, 'wb')
                fp.write(part.get_payload(decode=True))
                fp.close()
        
        
        copy_and_delete(m,emailid,"pdc")

        #save info.txt
        info_path=os.path.join(save_dir, "info.txt")
        f_info=open(info_path,"wb")
        f_info.write("\n".join(line))
        f_info.close()
        
        os.rename(save_dir,os.path.join(detach_dir,"r"+id))
        #append to queue.txt
        f=open('control/download.txt','a')
        f.write("~".join(line)+"\n")
        f.close()
        
    
    
    m.close()
    m.logout()

def copy_and_delete(m=None,emailid=None,dist_folder=None):
    print 'copy email to ', dist_folder
    m.copy(emailid, dist_folder)
    sleep(10)
    m.store(emailid, "+FLAGS.SILENT", '(\\Deleted)')
                    
def main():

    print "Monitoring the Mail server  "
    while 1:
        try:
           pass
        except Exception as inst:
            print inst.args

        print 'login, search and downlaod '
        login_search_download("UNSEEN")

        delay=10
        print "Sleeping for  " + str(delay) + "  seconds..."
        sleep(delay)

    
    


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        pass
