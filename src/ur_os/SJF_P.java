/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author prestamour
 */
public class SJF_P extends Scheduler {

    public SJF_P(OS os) {
        super(os);
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
        if (!cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
            
        }
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        if (!cpuEmpty) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
            
        }
    }

   
    @Override
    public void getNext(boolean cpuEmpty) {

        if (!processes.isEmpty() && cpuEmpty) {

            int minRemaining = Integer.MAX_VALUE;
            Process selected = null;

            for (Process process : processes) {

                if (process.isCurrentBurstCPU()) {

                    int remaining = process.getRemainingTimeInCurrentBurst();

                    if (remaining < minRemaining) {
                        minRemaining = remaining;
                        selected = process;

                    } else if (remaining == minRemaining && selected != null) {

                        if (os.SCHEDULER_TIEBREAKER_TYPE == TieBreakerType.LARGEST_PID) {
                            if (process.getPid() > selected.getPid()) {
                                selected = process;
                            }
                        } else { // SMALLEST_PID
                            if (process.getPid() < selected.getPid()) {
                                selected = process;
                            }
                        }
                    }
                }
            }

            if (selected != null) {
                processes.remove(selected);
                addContextSwitch();
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, selected);
            }
        }
    }
}
