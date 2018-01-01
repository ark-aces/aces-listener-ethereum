Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.provider "virtualbox" do |v|
    v.memory = 2048
    v.cpus = 4
  end

  config.vm.network "forwarded_port", guest: 8545, host: 8545

  config.vm.provision "shell", inline: <<-SHELL
    add-apt-repository -y ppa:ethereum/ethereum
    apt-get update

    apt-get install software-properties-common build-essential
    apt-get install ethereum solc

    # sync rinkby testnet:
    geth --rpc --rpcaddr=0.0.0.0 --rpcapi 'web3,eth,personal,debug' --rpccorsdomain="*" --rinkeby

    # geth attach ipc:/home/vagrant/.ethereum/rinkeby/geth.ipc
    # eth.syncing


    # sync mainnet
    geth --rpc --rpcaddr=0.0.0.0 --rpcapi 'web3,eth,personal,debug' --rpccorsdomain="*"

    # geth attach
    # eth.syncing
  SHELL
end