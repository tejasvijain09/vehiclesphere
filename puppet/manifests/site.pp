# =============================================================================
# VehicleSphere — Master Puppet Manifest (site.pp)
# Enforces target host state, security hardening, and container engine runtime
# =============================================================================

node default {
  include vehiclesphere::base
  include vehiclesphere::docker
  include vehiclesphere::firewall
}

class vehiclesphere::base {
  # Base required system utilities
  $packages = [
    'curl',
    'wget',
    'git',
    'htop',
    'unzip',
    'ca-certificates',
    'gnupg',
    'lsb-release',
  ]

  package { $packages:
    ensure => installed,
  }

  # Enforce deploy user exists
  user { 'ubuntu':
    ensure     => present,
    managehome => true,
    shell      => '/bin/bash',
  }

  # Ensure base project deployment directory
  file { '/opt/vehiclesphere':
    ensure => directory,
    owner  => 'ubuntu',
    group  => 'ubuntu',
    mode   => '0755',
  }
}

class vehiclesphere::docker {
  # Manage Docker system service
  service { 'docker':
    ensure  => running,
    enable  => true,
    require => Package['curl'],
  }

  # Enforce deploy user membership in docker group
  user { 'ubuntu_docker_group':
    name    => 'ubuntu',
    groups  => ['docker'],
    require => Service['docker'],
  }
}

class vehiclesphere::firewall {
  # Host firewall rules using ufw
  exec { 'enable_ufw':
    command => '/usr/sbin/ufw --force enable',
    unless  => '/usr/sbin/ufw status | grep -q "Status: active"',
  }

  exec { 'allow_ssh':
    command => '/usr/sbin/ufw allow 22/tcp',
    unless  => '/usr/sbin/ufw status | grep -q "22/tcp.*ALLOW"',
    require => Exec['enable_ufw'],
  }

  exec { 'allow_http':
    command => '/usr/sbin/ufw allow 80/tcp',
    unless  => '/usr/sbin/ufw status | grep -q "80/tcp.*ALLOW"',
    require => Exec['enable_ufw'],
  }

  exec { 'allow_https':
    command => '/usr/sbin/ufw allow 443/tcp',
    unless  => '/usr/sbin/ufw status | grep -q "443/tcp.*ALLOW"',
    require => Exec['enable_ufw'],
  }
}
