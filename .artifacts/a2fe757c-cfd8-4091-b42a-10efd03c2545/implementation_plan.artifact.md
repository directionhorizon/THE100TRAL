# Integration of Tavily.ai for Social Listening and Academic Research

This plan outlines the integration of the Tavily.ai API into the THE100TRAL platform to provide real-time web access, search, and research capabilities to the **Academic Agent** and **Social Media Agent**.

## User Review Required

> [!IMPORTANT]
> The Tavily API key provided (`tvly-dev-375g81-...`) will be stored securely in the app's `SecureSecretStore` (using `EncryptedSharedPreferences`).
> I will also attempt to configure a local "bridge" tool if possible, but the primary focus is the on-device agent capabilities.

## Proposed Changes

### Core Services

#### [NEW] [TavilyService.kt](./app/src/main/java/com/example/the100tral/core/network/TavilyService.kt)
Create a network service using OkHttp to interface with Tavily API (Search, Context, and Q&A endpoints).

#### [MODIFY] [MainActivity.kt](./app/src/main/java/com/example/the100tral/app/src/main/java/com/example/the100tral/MainActivity.kt)
- Register the `TAVILY_API_KEY` in `SecureSecretStore`.
- Instantiate `TavilyService`.
- Register the new `TavilySearchTool`.

---

### Tools & Agents

#### [NEW] [TavilySearchTool.kt](./app/src/main/java/com/example/the100tral/core/tool/impl/TavilySearchTool.kt)
A new tool that agents can use to perform web searches, extract content, and perform research.

#### [MODIFY] [SocialListeningTool.kt](./app/src/main/java/com/example/the100tral/core/tool/impl/SocialListeningTool.kt)
Replace the simulated sentiment analysis with real-time web scanning using Tavily.

#### [MODIFY] [AcademicAgent.kt](./app/src/main/java/com/example/the100tral/platform/departments/academic/AcademicAgent.kt)
Integrate the `TavilySearchTool` to allow the agent to perform real "State of the Art" research instead of just simulation.

#### [MODIFY] [SocialMediaAgent.kt](./app/src/main/java/com/example/the100tral/platform/departments/marketing/sub/SocialMediaAgent.kt)
Allow the agent to use Tavily for trend detection and brand monitoring.

---

### Infrastructure (Agent Skills)

#### [NEW] [tavily_setup.sh](./.artifacts/a2fe757c-cfd8-4091-b42a-10efd03c2545/scratch/tavily_setup.sh)
A scratch script to automate the CLI and Skills setup on the host machine if the environment permits.

## Verification Plan

### Automated Tests
- Unit test for `TavilyService` (using MockWebServer if available, or simple Mockito).
- Validation of API key retrieval from `SecureSecretStore`.

### Manual Verification
- Run the app and trigger a request to the Academic Agent (e.g., "What are the latest breakthroughs in AI Agents?").
- Verify logs to see Tavily API calls and real data being processed.
- Check the Social Media Agent's response to see if it incorporates real-world trends.
