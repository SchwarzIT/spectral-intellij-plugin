package com.schwarzit.spectralIntellijPlugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


@State(
    name = "com.schwarzit.spectralIntellijPlugin.settings.ApplicationSettingsState",
    storages = [Storage("spectral.xml")]
)
class ApplicationSettingsState : PersistentStateComponent<ApplicationSettingsState> {

    var ruleset: String = "https://raw.githubusercontent.com/SchwarzIT/api-linter-rules/main/spectral-api.yml"

    companion object {
        val instance: ApplicationSettingsState
            get() = ApplicationManager.getApplication().getService(ApplicationSettingsState::class.java)
    }

    override fun getState(): ApplicationSettingsState {
        return this
    }

    override fun loadState(state: ApplicationSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
