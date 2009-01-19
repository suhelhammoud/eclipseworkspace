import aiml , time

    

def start_application():    
     ###init robot
    k = aiml.Kernel()
    k.learn("std-startup.xml")
    #k.learn("bear-startup.xml")

    k.setPredicate("secure", "yes")
    k.respond("load aiml b")
    
    
    while True:
        try:
            user_msg= raw_input("you say <- ")
            print "Globus -> ", k.respond(user_msg)
        except :
            print "Runtime Error"
    

    

if __name__ == "__main__":
    start_application()