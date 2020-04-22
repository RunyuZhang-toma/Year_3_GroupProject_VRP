### Year_3_GroupProject_VRP

This is a repository for UNNC 2018-2019 computer science group project: UNNC UAV Patrol System This project is used in UNNC internal network and based on DJI UAV(mobile SDK)

---

Group member:

* Runyu Zhang (design software framework)
* Huixing Ren (Android application and DJI SDK)
* Yinglun Li (front-end webpage)
* Danyun Wang (map API and patrol webpage)
* Zeyu Zhang (back-end php server in UNNC computer science server and mySQL database)
* Qichen Zhang (DJI SDK)

---

Software design aim

Tasks and Deliverables In this project, the team is asked to implement an unmanned aerial vehicle patrolling system with a fleet of at least two aerial vehicles. In particular, you are asked to 

1) design and implement a webpage system that can communicate and control the basic control of an aerial vehicles, including take off, landing, position control through SDK provided by the aerial vehicle provider. The planned UAV used in this project is DJI Matrice Series mounted with Manifold onboard SDK. See details from https://www.dji.com/manifold?site=brandsite&from=landing_page. 

2) implement a machine learning based automatic object recognition module from videos taken by an UAV onboard camera. 

3) implement a system that automatically track a single object (extra bonus) from UAV onboard video stream 

---

Algorithm and result

This algorithm access the map location data from UNNC CSserver mySQL database and return location order to mySQL. The code was implemented in Java and Json string. The back-end server call this algorithm by command line and wait for database result. Due to the map size restrict to UNNC campus for security issues, the location maximum sites are 50 and calculate less than 10 seconds. This VRP use genetic algorithm to solve this campus patrol system and enable vehicles to 5. Very details in the pdf GRP Final Report.
