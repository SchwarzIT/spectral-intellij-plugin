package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState",
    storages = [Storage("spectral.xml")]
)
@Service(Service.Level.PROJECT)
class ProjectSettingsState : PersistentStateComponent<ProjectSettingsState> {

    var ruleset: String = "https://raw.githubusercontent.com/baloise-incubator/spectral-ruleset/refs/heads/main/zalando.yml"
    var includedFiles: String = """
        **/*openapi.json
        **/*openapi.yml
        **/*openapi.yaml
    """.trimIndent()

    override fun getState(): ProjectSettingsState {
        return this
    }

    override fun loadState(state: ProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
