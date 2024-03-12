# CASEVAC

## 1 Identification  
Description: Ground based autonomous systems used in Casualty Evacuation (CASEVAC)  
M&S Domain: [acquisition, intelligence, training, testing, analysis, experimentation]  

Table 1. POCs  
Name | Organization | Role | Email  
-----|--------------|------|------------------------  
Sean Johnson | Hyssos | POC | Sean.johnson@hyssos.com  
Paulo Barthelmess | Hyssos | PI | Paulo.barthelmess@hyssos.com   

Relationship to other use cases:  
- <use case ID> - <Description of relationship>  
- <use case ID> - <Description of relationship>  

## 2 Goals and Measures of Performance (MOPs)  
<What is /are the end goal(s) and how will we measure its / their achievement?>  
2.1 Goals  
1. Safely transport an injured warfighter to medical evacuation point using unmanned ground vehicles, reducing risk to additional personnel  
2. <goal 2> …  

2.2 Measures of Performance (MOPs)  
1. Of ability to autonomously establish a safe route from collection to evacuation point  
2. Of ability to follow safe routetransporting injured warfighter  
3. Time needed for human operator, "on-the-loop" to accurately convey CASEVAC mission plan, monitor, query, and correct autonomous behavior  
4. Time needed to achieve a mission  

## 3 Scenario Exemplar  
Two UGVs are used in coordination to transport an injured warfighter to a medical evacuation point: a scouting UGV and a Medical Transport UGV. The scouting UGV travels to the medical evacuation point, identifying a safe route. Upon encountering an obstacle (such as a broken bridge), or upon receiving information about a potential threat (suspected IED along a road), the scout autonomously plans an alternative route, taking into consideration the requirements of the Medical Transport UGV. When the scout successfully arrives at the medical evacuation point, it relays the route to the Medical Transport UGV, which then navigates through that route carrying the injured warfighter.  

## 4 Scenario Steps  
A soldier receives a CASEVAC request and then tasks an autonomous CASEVAC team (scout vehicle + evac vehicle with stretchers).  
1. Soldier creates task to evacuate casualties from a specific location  
   a. Information needed  
      i. Tactical task (order)  
      ii. Location  
1. Task is sent to the team of autonomous vehicles via C2SIM ASX orders  
   a. Information needed  
      i. Send/receive order  
1. Task sent to location of CASEVAC receiving the vehicles  
1. A scout vehicle:  
   a. Plans a route to the evacuation point  
      i. Handled by autonomy  
   b. Moves along the route to verify that it is safe  
      i. Handled by autonomy  
   c. Replans route if threat or obstacle is encountered (suspected IED reported along the original route, fallen bridge is found)  
      i. Handled by autonomy  
   d. Shares its status and position throughout execution  
      i. Information needed  
         1. Message back to human on the loop HOTL  
   e. Publishes route once it safely traverses to the evacuation point  
      i. Handled by autonomy  
1. The transport vehicle:  
   a. Awaits for the scout vehicle to advertise the safe route  
      i. Information needed  
         1. Machine to machine msg  
   b. Moves to collection point to have the injured warfighter loaded on stretcher  
      i. Handled by autonomy  
   c. Follows the route identified by the scout vehicle  
      i. Handled by autonomy  

1. The soldier remains “on-the-loop” monitoring the situation  
   a. Providing mission update commands as new information arrives  
      i. Information needed  
         1. Msg to autonomous system(s)  
   b. Receiving updates from autonomous CASEVAC team (in human understandable messages)  
      i. Information needed  
         1. Msg from AS to HOTL  
1. The autonomous CASEVAC team sends “explainable reasons” to changes in mission orders  
   a. Why did it change its expected route to/from site  
   b. Why was it unable to complete its mission  
   c. General mission updates  
   d. Expected time to complete phased tasks  

## 5 Conceptual Model  
<Describe entities and actions being represented, e.g.  
- Collection point, evacuation point  
- Route  
- Obstacles  
- Threats  
Describe level of resolution necessary for entities, e.g. effects, frequency, bandwidth, beam parameters, waveforms. Consider representing actions graphically as vignettes or interactions.>  

## 6 Potential components  
<Description of potential components (not necessarily identification of specific components), e.g.  
- Autonomous vehicles  
- Tasking and control interface  

## 7 Derived Data Exchange Model (DEM) Requirements  
<Identify  
- Data that needs to be represented  
- Format / level of detail of data necessary to support conceptual model>  

## 8 Notes, Anomalies, Challenges  

## 9 References  

## 10 Acronyms  