provider "docker" {}

resource "docker_image" "backend" {
  name         = "spring-backend"
  build {
    context    = "../backend"
    dockerfile = "Dockerfile"
  }
}

resource "docker_container" "backend" {
  name  = "spring-backend"
  image = docker_image.backend.latest
  ports {
    internal = 8080
    external = 8080
  }
}
