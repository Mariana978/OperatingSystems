/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import static ur_os.InterruptType.SCHEDULER_CPU_TO_RQ;
import static ur_os.InterruptType.SCHEDULER_RQ_TO_CPU;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler{

    int q;
    int cont;
    boolean multiqueue;
    
    RoundRobin(OS os){
        super(os);
        q = 5;
        cont=0;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean multiqueue){
        this(os);
        this.q = q;
        this.multiqueue = multiqueue;
    }
    

    
    void resetCounter(){
        cont=0;
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        //si CPU vacío
        if (cpuEmpty) {
            if (!processes.isEmpty()) {
                Process siguiente = processes.removeFirst();
                os.interrupt(SCHEDULER_RQ_TO_CPU, siguiente);
                addContextSwitch();
                resetCounter();
            }
            return;
        }
        // CPU ocupaditoo
        cont++;
        Process actual = os.getProcessInCPU();
        if (actual.isFinished()) {
            resetCounter();
            return;
        }
        // Si no ha llegado al quantum, siguee
        if (cont < q) {
            return;
        }
        // Se acabó el quantum
        if (!processes.isEmpty()) {
            Process siguiente = processes.removeFirst();
            os.interrupt(SCHEDULER_CPU_TO_RQ, siguiente);
            addContextSwitch();
        }
        resetCounter();
    }
 
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}
