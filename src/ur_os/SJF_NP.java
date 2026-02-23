/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class SJF_NP extends Scheduler{

    
    SJF_NP(OS os){
        super(os);
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!cpuEmpty || processes.isEmpty()) {
            return;
        }

        Process shortest = processes.get(0);

        for (Process p : processes) {

            int pTime = p.getRemainingTimeInCurrentBurst();
            int sTime = shortest.getRemainingTimeInCurrentBurst();

            if (pTime < sTime) {
                shortest = p;
            } 
            else if (pTime == sTime) {
                shortest = tieBreaker(p, shortest);
            }
        }

        processes.remove(shortest);
        os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortest);
        
        //Insert code here
        
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive
    
}

