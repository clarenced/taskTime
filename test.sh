#!/bin/zsh

gcloud compute instances create worker-apps \
    --project=clarenced-cloud \
    --zone=europe-west9-b \
    --machine-type=e2-standard-4 \
    --network-interface=network-tier=PREMIUM,nic-type=VIRTIO_NET,stack-type=IPV4_ONLY,subnet=private-cloud-subnet \
    --metadata=enable-osconfig=TRUE,ssh-keys=clarence.dimitri.charles:ssh-ed25519\ AAAAC3NzaC1lZDI1NTE5AAAAIIj/p5/K2ZVea2qmjmAsR\+B7M1QDE\+\+qVRQgHEU4tpAq\ clarence.dimitri.charles@gmail.com \
    --maintenance-policy=MIGRATE --provisioning-model=STANDARD \
    --service-account=932822551981-compute@developer.gserviceaccount.com \
    --scopes=https://www.googleapis.com/auth/devstorage.read_only,https://www.googleapis.com/auth/logging.write,https://www.googleapis.com/auth/monitoring.write,https://www.googleapis.com/auth/service.management.readonly,https://www.googleapis.com/auth/servicecontrol,https://www.googleapis.com/auth/trace.append \
    --create-disk=auto-delete=yes,boot=yes,device-name=control-plane,image=projects/ubuntu-os-cloud/global/images/ubuntu-2404-noble-amd64-v20250527,mode=rw,size=20,type=pd-balanced \
    --no-shielded-secure-boot \
    --shielded-vtpm \
    --shielded-integrity-monitoring \
    --labels=goog-ops-agent-policy=v2-x86-template-1-4-0,goog-ec-src=vm_add-gcloud \
    --reservation-affinity=any && \
  printf 'agentsRule:\n  packageState: installed\n  version: latest\ninstanceFilter:\n  inclusionLabels:\n  - labels:\n      goog-ops-agent-policy: v2-x86-template-1-4-0\n' > config.yaml && \
gcloud compute instances ops-agents policies create goog-ops-agent-v2-x86-template-1-4-0-europe-west9-b \
  --project=clarenced-cloud \
  --zone=europe-west9-b \
  --file=config.yaml && \
gcloud compute resource-policies create snapshot-schedule default-schedule-1 --project=clarenced-cloud \
  --region=europe-west9 \
  --max-retention-days=14 \
  --on-source-disk-delete=keep-auto-snapshots \
  --weekly-schedule \
  --start-time=09:00 && \
gcloud compute disks add-resource-policies control-plane \
  --project=clarenced-cloud \
  --zone=europe-west9-b \
  --resource-policies=projects/clarenced-cloud/regions/europe-west9/resourcePolicies/default-schedule-1