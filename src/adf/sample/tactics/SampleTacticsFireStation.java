package adf.sample.tactics;

import adf.agent.communication.MessageManager;
import adf.agent.develop.DevelopData;
import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.agent.module.ModuleManager;
import adf.agent.precompute.PrecomputeData;
import adf.component.centralized.CommandPicker;
import adf.component.communication.CommunicationMessage;
import adf.component.module.complex.TargetAllocator;
import adf.component.tactics.TacticsFireStation;
import rescuecore2.worldmodel.EntityID;

import java.util.Map;

public class SampleTacticsFireStation extends TacticsFireStation {
    private TargetAllocator allocator;
    private CommandPicker picker;

    @Override
    public void initialize(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, ModuleManager moduleManager, MessageManager messageManager, DevelopData debugData) {
        switch  (scenarioInfo.getMode()) {
            case PRECOMPUTATION_PHASE:
                this.allocator = moduleManager.getModule(
                        "TacticsFireStation.TargetAllocator",
                        "adf.sample.module.complex.SampleFireTargetAllocator");
                this.picker = moduleManager.getCommandPicker(
                        "TacticsFireStation.CommandPicker",
                        "adf.sample.centralized.CommandPickerFire");
                break;
            case PRECOMPUTED:
                this.allocator = moduleManager.getModule(
                        "TacticsFireStation.TargetAllocator",
                        "adf.sample.module.complex.SampleFireTargetAllocator");
                this.picker = moduleManager.getCommandPicker(
                        "TacticsFireStation.CommandPicker",
                        "adf.sample.centralized.CommandPickerFire");
                break;
            case NON_PRECOMPUTE:
                this.allocator = moduleManager.getModule(
                        "TacticsFireStation.TargetAllocator",
                        "adf.sample.module.complex.SampleFireTargetAllocator");
                this.picker = moduleManager.getCommandPicker(
                        "TacticsFireStation.CommandPicker",
                        "adf.sample.centralized.CommandPickerFire");

        }
    }

    @Override
    public void think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, ModuleManager moduleManager, MessageManager messageManager, DevelopData debugData) {
        this.allocator.updateInfo(messageManager);
        this.picker.updateInfo(messageManager);
        Map<EntityID, EntityID> data = this.allocator.calc().getResult();
        for(CommunicationMessage message : this.picker.setData(data).calc().getResult()) {
            messageManager.addMessage(message);
        }
    }

    @Override
    public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, ModuleManager moduleManager, PrecomputeData precomputeData, DevelopData debugData) {
        this.allocator.resume(precomputeData);
        this.picker.resume(precomputeData);
    }

    @Override
    public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, ModuleManager moduleManager, DevelopData debugData) {
        this.allocator.preparate();
        this.picker.preparate();
    }
}