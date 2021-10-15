# poll-system

1. Make sure to create a deployment by clicking "Edit Configuration" -> "Deployment" -> "add" -> "Artifact"
2. Should say "PollSystem_war"
3. Delete the name in "Application Context"
4. Try accessing the Servlet through `http://localhost:8080/api/votes` and `http://localhost:8080/api/poll` and it should hit the doGet of each servlet. 