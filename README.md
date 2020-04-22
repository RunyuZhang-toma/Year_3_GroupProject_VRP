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

---

Genetic algorithm

Key point:

* Crossover
  * The crossover is order cross order(OX) and following is the principle explanation. 
    1. Select a section(continuous) with several gene from parent1 and place it in the same location in the child chromosome.
    2.  Use the parent2 to loop test the each element if is not exist in the child chromosome, add the element into child orderly from start to end.
    3. If want to get second child chromosome in once crossover, then exchange the parent1 and parent2 could occur another child chromosome. 
    4. Attention: this method do not need to test conflict like Partial-Mapped Crossover(PMX).
    
* Mutation
  * In this section, the theory is similar with crossover, the difference is that this mutation happened in a single chromosome.
    1.  Randomly select two locations or two continuous series locations
    2. Then use a temp location to replace the two locations.
* Initialize the population:
  * In this section, every chromosome is randomly created by following procedure.
    1.  Set up a blank chromosome and add random number in the route.
    2.  Sort the number in the route.
    3. According to the sort to arrange the location number into correspond size.


