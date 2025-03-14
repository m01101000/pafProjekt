# 1. Basis-Image mit Node.js für den Build
FROM node:18-alpine

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

# 2. Falls du Cypress nutzt, installiere es
RUN npm install cypress --save-dev

# 3. Starte Angular mit `ng serve`
CMD ["npm", "run", "start"]
