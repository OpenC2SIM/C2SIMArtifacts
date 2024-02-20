
# Scenario extracted from paper

Biaginni, M., Corona, F., Innocenti, F., Marcovaldi, S. (2018) Requirements and Example for a C2SIM Extension to Unmanned Autonomous Systems (UAXS). NATO M&S COE Annual Review Vol. 2, Chapter 8, 2018, p93

## GPT-4 Results

>**Scenario Summary Narrative**
>
>The R2CD2 project scenario involves a multifaceted urban environment operation targeting a vehicle-borne improvised explosive device (IED) threat in a future megacity. Autonomous systems, including both aerial (UAaS) and ground (UAgS) units, are deployed to identify and neutralize this threat. The police initially detect and identify the suspicious vehicle, relaying information for further reconnaissance and neutralization. Two distinct autonomous systems teams are formed: a swarm of UAaS for reconnaissance to determine the threat's location and a UAgS team tasked with the vehicle's neutralization.
>
>**Specific Goals to be Achieved by the Systems**
>
>*Interaction and Interoperability*: Enable seamless communication and interaction between simulated Unmanned Autonomous Systems (UAxS) and real Command and Control (C2) systems using the C2SIM Interoperability Language for messaging.
>
>*UAxS Employment Study*: Investigate the deployment of UAxS within a densely populated urban setting of the future, focusing on roles in Air and Land operational domains, particularly in an urban counter-IED mission.
>
>*Autonomy and Decision-making*: Explore the autonomy levels appropriate for UAxS, enabling them to make decisions based on environmental feedback without needing continuous human intervention or coordination.
>
>**Measures of Performance**
>
>- *Accuracy of Threat Identification*: The UAaS's ability to correctly identify and locate the threat.
>- *Efficiency of Threat Neutralization*: The UAgS team's success in neutralizing the detected threat with minimal collateral damage and risk.
>- *Interoperability and Communication*: Evaluation of data exchange and orders/reporting efficiency between UAxS and C2 systems.
>- *Autonomy*: The degree to which UAxS can independently perform assigned tasks based on predefined autonomy levels and environmental feedback.
>
>**Scenario Steps**
>
>1. *Initial Threat Identification*: Police patrols detect a vehicle suspected of carrying explosives and communicate its description for further action.
>
>1.*Deployment of UAaS*: A swarm of small UAaS equipped with electro-optical sensors are deployed to locate the suspicious vehicle within the urban environment.
>
>1. *Reconnaissance and Reporting*: Upon identifying the vehicle, the UAaS relay position and status information back to their command post, initiating the next phase of the operation.
>
>1. *Deployment of UAgS*: A ground team of UAgS, equipped for C-IED missions in potentially contaminated areas, is activated to approach and neutralize the vehicle.
>
>1. *Mission Execution and Autonomy*: Each autonomous team executes its mission based on the level of autonomy suitable for the task, reporting back to command as required or making decisions independently as conditions dictate.
>
>**>Conceptual Model**
>1. *Identification of Domain and Scope*
>
>   - Domain: Military operations involving unmanned autonomous systems (UAxS) in urban environments.
>
>   - Scope: Interaction between UAxS and C2 systems, employment of UAxS for reconnaissance and neutralization tasks in urban counter-IED missions, and examination of UAxS autonomy levels.
>
>2. *Concept Identification*
>
>- Autonomous Systems (subclasses: UAaS, UAgS)
>- Command and Control (C2) Systems
>- Mission (subclasses: Reconnaissance, Neutralization)
>- Threat (attributes: location, type)
>- Environment (attributes: urban, future megacity)
>
>3. *Define the Hierarchy*
>- Autonomous Systems
>   - UAaS
>   - UAgS
>- Mission
>   - Reconnaissance
>   - Neutralization
>
>4. *Relationship Identification*
>- Autonomous Systems receive orders from C2 Systems.
>- Reconnaissance Mission informs Neutralization Mission.
>- Autonomous Systems operate within Environment.
>- Autonomous Systems target Threat.
>
>5. *Property Identification*
>
>- Autonomous Systems: Type (aerial, ground), Equipment (sensors, explosives).
>- Environment: Type (urban), Era (future), Challenges (high population density, natural disasters).
>- Threat: Type (vehicle-borne IED), Movement (mobile, stationary).
>
>6. *Identify the Constraints/Rules*
>
>- UAaS must complete Reconnaissance Mission before UAgS begins Neutralization Mission.
>- Autonomous Systems must communicate effectively with C2 Systems.
>- Autonomous Systems must adapt actions based on Level of Autonomy and environmental feedback.
