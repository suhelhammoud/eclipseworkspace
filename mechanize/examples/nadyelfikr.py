import feedparser, mechanize

###List of sites scanned. You can add more !
sites=["http://www.alarabiya.net/rss/PoliticsPage.xml",\
       "http://www.asharqalawsat.com/rss/AAARSS4.xml",\
       "http://newsrss.bbc.co.uk/rss/arabic/middle_east_news/rss.xml", \
       ]

### links to be posted at the forum
links=[]

### Fill in links
for site in sites:
    parsed_site = feedparser.parse(site)
    for i in parsed_site.entries:
        links.append(i.link.encode('ascii'))


###Login nadyelfikr.com
b = mechanize.Browser(factory=mechanize.DefaultFactory(i_want_broken_xhtml_support=True))
b.set_handle_robots(False)
response=b.open("http://www.nadyelfikr.com")
print response.geturl()
b.select_form(nr=1)
b["UserName"] = 'globus'
b["PassWord"] = "#################"
b.submit()

###Post the links one by one
url="http://www.nadyelfikr.net/index.php?act=post&do=reply_post&f=144&t=53182"
for link in links:
    post_text(link,url, b)

    
def post_text(txt=":)", url=None, b=None):
    b.open(url)
    b.select_form("REPLIER")
    b["Post"] =txt
    control = b.form.find_control("attachgo")
    control.disabled= True
    b.submit()    

# Read a subject for 100 times :)
r=b.open("http://www.nadyelfikr.net/index.php?showtopic=53182")
for i in range(500):
    b.reload()
    print i

