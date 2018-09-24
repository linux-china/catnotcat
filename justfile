# start the server
start:
  ./mvnw -q spring-boot:run -Dspring.cloud.gcp.credentials.location=file:///$HOME/catnotcat.json

# test cat
test_cat:
  curl -Lo cat.jpg https://upload.wikimedia.org/wikipedia/commons/thumb/4/4f/Felis_silvestris_catus_lying_on_rice_straw.jpg/320px-Felis_silvestris_catus_lying_on_rice_straw.jpg
  openssl base64 -in cat.jpg | curl -H"Content-Type: text/plain" -w "\n"  -d@- localhost:8080

# test not cat, it's a dog
test_notcat:
  curl -Lo dog.jpg https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Golden_Retriever_medium-to-light-coat.jpg/320px-Golden_Retriever_medium-to-light-coat.jpg
  openssl base64 -in dog.jpg | curl -H"Content-Type: text/plain" -w "\n" -d@- localhost:8080


  