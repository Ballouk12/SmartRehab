import React, { useState } from 'react';

export function PatientForm() {
  const [formData, setFormData] = useState({
    prenom: '',
    nom: '',
    email: '',
    login: '',
    password: '',
  });

  const createPatient = async (patientData) => {
    try {
      const response = await fetch("http://localhost:8083/api/patients/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(patientData), // Convertit les données du patient en JSON
        credentials: "include", // Inclut les informations d'identification si nécessaire
      });

      if (!response.ok) {
        throw new Error(`Erreur: ${response.status}`);
      }
      setFormData({
        prenom: '',
        nom: '',
        email: '',
        login: '',
        password: '',
        login: '',
        password: '',
      });
      const data = await response.json();
      return data; // Retourne le patient créé
    } catch (error) {
      console.error("Erreur lors de la création du patient :", error);
      throw error; // Relance l'erreur pour la gestion en amont
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    createPatient(formData);
  };

  return (
    <div className="bg-white shadow sm:rounded-lg w-1/2 self-center">
      <div className="px-4 py-5 sm:p-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900">New Patient</h3>
        <div className="mt-6">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">First Name</label>
                <input
                  type="text"
                  required
                  value={formData.prenom}
                  onChange={(e) => setFormData({ ...formData, prenom: e.target.value })}
                  className="p-2 mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Last Name</label>
                <input
                  type="text"
                  required
                  value={formData.nom}
                  onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                  className="p-2 mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Email</label>
              <input
                type="email"
                required
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                className="p-2 mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Login</label>
              <input
                type="text"
                required
                value={formData.login}
                onChange={(e) => setFormData({ ...formData, login: e.target.value })}
                className="p-2 mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Password</label>
              <input
                type="password"
                required
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                className="p-2 mt-1 block w-full rounded-md border border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 focus:ring-1"
              />
            </div>

            <button
              type="submit"
              className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Save Patient
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}