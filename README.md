Simularea Cozilor Și Gestiunea Acestora În Supermarkets – Proiect Java
Acest proiect a fost realizat cu scopul de a simula modul în care funcționează mai multe cozi într-un sistem de așteptare, pornind de la o idee simplă: clienții ajung într-un punct de acces și sunt direcționați spre una dintre mai multe cozi, în funcție de anumite criterii. Totul este gândit să reflecte o logică reală de distribuire echilibrată, astfel încât timpul total de așteptare să fie minimizat.

Simularea a fost construită în Java, utilizând o interfață grafică simplă cu Swing pentru a permite utilizatorului să pornească simularea și să vizualizeze modul în care clienții sunt alocați în cozi. Fiecare client are un timp de sosire (arrivalTime) și un timp de servire (serviceTime), iar la momentul sosirii, acesta este direcționat către coada care are, în acel moment, cel mai mic timp total de așteptare.

Distribuirea clienților se face cu ajutorul clasei SelectionStrategy, care sortează clienții cu același timp de sosire în funcție de timpul de servire, pentru a optimiza echilibrul între cozi. Clasa Scheduler este responsabilă de monitorizarea acestui proces și de calculul unor statistici importante, precum timpul mediu de așteptare.

Clienții sunt generați automat cu ajutorul clasei GeneratorQC, iar aceștia sunt apoi procesați și alocați în cozi (Queues) pe parcursul simulării. În interfață, utilizatorul poate vizualiza fiecare coadă în parte și timpul ei de așteptare, iar în consolă se afișează și detalii legate de alocarea fiecărui client.

Acest proiect reprezintă un exemplu aplicat de utilizare a principiilor de programare orientată pe obiect, sincronizare și algoritmi de optimizare a resurselor. De asemenea, el oferă o bază solidă pentru dezvoltări ulterioare mai complexe, cum ar fi simularea timpului în timp real, introducerea de priorități pentru clienți, sau integrarea unor scenarii dinamice.
