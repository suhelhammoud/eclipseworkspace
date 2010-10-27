'''
Created on 11 Oct 2009

@author: suhel hammoud
'''

from xml.dom.minidom import parse, parseString
import sys


def getName(nodelist):
    rc = []
    for node in nodelist:
        if node.nodeType == node.name:
            rc.append(node.childNodes[0])
    return ''.join(rc)

def getValue(nodelist):
    rc = []
    for node in nodelist:
        if node.nodeType == node.value:
            rc.append(node.childNodes[0])
    return ''.join(rc)

def getProperty(p):
    return (str(p.childNodes[0].childNodes[0].data),
            str( p.childNodes[1].childNodes[0].data))
    
def extract(nodelist):
    result= {}
    for node in nodelist:
        name,value = getProperty(node)
        result[name]=value
    return result

def extractfile(filename='job.xml'):
    datasource = open(filename)
    dom = parse(datasource)   # parse an open file
    properties = dom.getElementsByTagName('property')
    return extract(properties)



def mapKeys():
    return {
            'mapred.output.compress':'useCompression',
            'io.sort.mb':'ioSortMb',
            'io.sort.record.percent':'ioSortRecordPercent',
            'io.sort.factor':'ioSortFactor',
            'io.sort.spill.percent':'ioSortSpillPercent',
            'mapred.reduce.parallel.copies':'mapReduceParallelCopies',
            'mapred.child.java.opts':'mapredChildJavaOpts',
            'mapred.job.shuffle.input.buffer.percent':'mapredJobShuffleInputBufferPercent',
            'mapred.job.shuffle.merge.percent':'mapredJobShuffleMergePercent',
            'mapred.inmem.merge.threshold':'mapredInmemMergeThreshold',
            
            'mapred.tasktracker.map.tasks.maximum':'mapTasksMax',
            'mapred.tasktracker.reduce.tasks.maximum':'reduceTasksMax'
    }
def mapTypes():
    return {
            'mapred.output.compress':'bool',
            'io.sort.mb':'float',
            'io.sort.record.percent':'float',
            'io.sort.factor':'int',
            'io.sort.spill.percent':'float',
            'mapred.reduce.parallel.copies':'int',
            'mapred.child.java.opts':'int',
            'mapred.job.shuffle.input.buffer.percent':'float',
            'mapred.job.shuffle.merge.percent':'float',
            'mapred.inmem.merge.threshold':'int',
            
            'mapred.tasktracker.map.tasks.maximum':'int',
            'mapred.tasktracker.reduce.tasks.maximum':'int'
    }


def extract_and_map(filename='job.xml', outfile=None):
    if outfile==None:
        outfile=filename+'-out.txt'
    result=""
    allconfs= extractfile(filename)
    mapnames=mapKeys()
    for k in mapnames:
        result+= '"'+mapnames[k]+'":'+allconfs[k]+"\n"
    file =open(outfile,'w')
    file.write(result)
    file.close()

def extract_and_map(filename='job.xml', outfile=None):
    if outfile==None:
        outfile=filename+'-out.txt'
    result=""
    allconfs= extractfile(filename)
    mapnames=mapKeys()
    for k in mapnames:
        result+= '"'+mapnames[k]+'":'+allconfs[k]+"\n"
    file =open(outfile,'w')
    file.write(result)
    file.close()


if __name__ == '__main__':
    args=[]
    for a in sys.argv:
        args.append(a)

    print args

    if len(args) < 3 :
        print 'not enough parameters'
        print 'job-conf-extract.py in_file out_file'
        exit()
    else:
        infile = args[1]
        outfile= args[2]
        extract_and_map(infile,outfile)

        print 'done'

#print [getName(i) for i in property]