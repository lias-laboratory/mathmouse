Vagrant.configure(2) do |config|
  config.vm.hostname = "liasvagrant-docker"
  config.vm.provider :virtualbox do |vb|  
  config.vm.network "forwarded_port", host: 5432, guest: 5432
  config.vm.network "forwarded_port", host: 15672, guest: 15672
  config.vm.network "forwarded_port", host: 5672, guest: 5672
  config.vm.network "forwarded_port", host: 2376, guest: 2376
  config.vm.network "forwarded_port", host: 5000, guest: 5000
  config.vm.box = "ubuntu/xenial64"
    vb.customize ["modifyvm", :id,
            "--name", "liasvagrant",
            "--memory", "2048",
            "--natdnshostresolver1", "on"]
   
  end
  
  config.vm.provision :shell, path: "bootstrap.sh"
end
