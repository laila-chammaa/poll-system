## Setup ✨
### Backend:

1. Make sure to create a deployment by clicking "Edit Configuration" -> "Deployment" -> "add" -> "Artifact"

2. Should say "PollSystem_war"
 
3. Delete the name in "Application Context"

4. Try accessing the Servlet through http://localhost:8080/api/votes and http://localhost:8080/api/poll and it should hit the doGet of each servlet.

5. Create polllib.jar by building the polllib project separately. Make sure you're generating a jar by going to File -> Project Structure -> Artifacts -> + JAR

5. Include the polllib.jar in your libraries by clicking File -> Project Structure -> Libraries.

### Frontend:

`npm install`

`npm start`

## Team 🦄
| Name | Github Username |
|---|---|
| Laila Chamma'a | [laila-chammaa](https://github.com/laila-chammaa) |
| Lisa Duong | [lisa7012](https://github.com/lisa7012) |
| Anthony Wong | [anthonywong0623](https://github.com/anthonywong0623) |
