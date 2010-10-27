# cluster conf
python generate_jobs.py tmpl-cluster var-cluster cluster/

# generate racks
python generate_jobs.py tmpl-rack cluster/template-ready.json cluster/racks


mkdir cluster/1 cluster/2 cluster/3 cluster/4
mkdir cluster/1/rack cluster/2/rack cluster/3/rack cluster/4/rack 

# copy racks
cp cluster/racks/rack-500g.json cluster/1/rack/rack.json
cp cluster/racks/rack-1t.json cluster/2/rack/rack.json
cp cluster/racks/rack-100t.json cluster/3/rack/rack.json
cp cluster/racks/rack-1p.json cluster/4/rack/rack.json

#copy jobs
cp tmpl-job/tmpl-job-1 cluster/1/tmpl-job
cp tmpl-job/tmpl-job-2 cluster/2/tmpl-job
cp tmpl-job/tmpl-job-3 cluster/3/tmpl-job
cp tmpl-job/tmpl-job-4 cluster/4/tmpl-job

python generate_jobs.py cluster/1/tmpl-job var-cluster cluster/1/jobs
python generate_jobs.py cluster/2/tmpl-job var-cluster cluster/2/jobs
python generate_jobs.py cluster/3/tmpl-job var-cluster cluster/3/jobs
python generate_jobs.py cluster/4/tmpl-job var-cluster cluster/4/jobs
