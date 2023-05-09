# IT-Conference
CONFERENCE LECTURES URLS:

1. ADD LECTURE TO DATABASE, AND CHECK IF THE INPUTED LECTURE MEETS THE CONDITION OF THE CONFERENCE:
  Request URL: POST http://localhost:8080/conference/lectures/addLecture
  Request body:
  {
   "topic": "Azure Cloud",
    "startTime": "14:00",
    "endTime": "15:45"
  }
  
2. RETURN ALL LECTURES:
  Request URL: GET http://localhost:8080/conference/lectures

3. RETURN INFORMATION ABOUT CHOSEN LECTURE:
  Request URL: GET http://localhost:8080/conference/lectures/lecture/1

4. MAKE RESERVATION TO PARTICIPANT FOR LECTURE:
  Request URL: POST http://localhost:8080/conference/lectures/lecture/1

  Request body:
  {
    "login": "exampleLogin",
   "email": "example@gmail.com"
  }

5. RETURN ALL INTERESTED PARTICIPANTS IN LECTURES (ALL PARTICIPANTS THAT HAVE AT LEAST 1 RESERVATION):
  Request URL: GET http://localhost:8080/conference/registered?login=admin


PARTICIPANTS URLS:

1. REGISTER PARTICIPANT:
  Request URL: POST http://localhost:8080/participant/registration

  Request body:
   {
    "firstName": "Example",
    "middleName": null,
    "lastName": "LastNameEX",
    "login": "yourLogin?",
    "email": "example@gmail.com"
   }
   
2. FIND PARTICIPANT BY ID:
  Request URL: GET http://localhost:8080/participant/find?id=1

3. CANCEL RESERVATION FOR PARTICIPANT:
  Request URL: DELETE http://localhost:8080/participant/reservations/cancel?login=example&lectureId=1
  
4. RETURNS PLAN OF CONFERENCE:
  Request URL: GET http://localhost:8080/participant/showPlan

5. SHOW ALL RESERVATIONS FOR THIS PARTICIPANT:
  Request URL: GET http://localhost:8080/participant/reservations?login=example

6. CHANGES EMAIL FOR THIS PARTICIPANT:
  Request URL: PUT http://localhost:8080/participant/changeEmail?id=1&email=ex@gmail.com
