# Weekly Dashboard Update (Non-Technical)

## What to edit each week
1. Open data.json.
2. Update these fields only:
   - complexityTrend.values
   - coverageByModule[*].coverage
   - codeChurnHotspots[*].changes
   - technicalDebtDistribution[*].value
   - overallHealthScore
   - sparklineTrends[*].values
   - churnHeatmap.files[*].values
   - sprintWin
   - nextPriority (optional)
3. Set meta.lastUpdated to the current date/time.

## Example timestamp format
Use: YYYY-MM-DDTHH:MM:SS
Example: 2026-04-14T09:00:00

## Save and view
1. Save data.json.
2. Open the folder with a local web server, then refresh code-health-dashboard.html.

Recommended simple options:
- VS Code Live Server: right-click code-health-dashboard.html, then choose Open with Live Server.
- Python (if installed): run `python -m http.server 8000` in the Code-Files folder, then open http://localhost:8000/code-health-dashboard.html

## Notes
- The dashboard reads from data.json automatically.
- Last updated is shown from meta.lastUpdated. If blank or invalid, the dashboard shows the current time.
