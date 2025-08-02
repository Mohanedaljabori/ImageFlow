terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0"
    }
  }
}

provider "docker" {}

resource "docker_image" "backend" {
  name = "spring-backend"
  build {
    context = "../file-upload-backend"
    dockerfile = "Dockerfile"
  }
}

resource "docker_container" "backend" {
  name  = "spring-backend"
  image = docker_image.backend.name
  ports {
    internal = 8080
    external = 8080
  }
}
