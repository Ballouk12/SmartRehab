import React, { useState } from 'react';

const Register = ({ onSwitchToLogin }) => {
    const [formData, setFormData] = useState({
    prenom: '',
    nom: '',
    login: '',
    password: ''
  });

  
const registerDoctor = async (doctorData) => {
  try {
    const response = await fetch('http://localhost:8083/api/doctors/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
       credentials: "include",
      body: JSON.stringify({
        prenom: doctorData.prenom,
        nom: doctorData.nom,
        login: doctorData.login,
        password: doctorData.password
      })
    });
  
    if (!response.ok) {
      throw new Error('Erreur lors de l\'enregistrement du docteur');
    }

    onSwitchToLogin();
    return newDoctor;
  } catch (error) {
    console.error('Erreur:', error);
    throw error;
  }
};


  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const doctor = await registerDoctor(formData);
      console.log('Docteur créé:', doctor);
      setFormData({
        prenom: '',
        nom: '',
        login: '',
        password: ''
      })
      // Gérer le succès (redirection, message, etc.)
    } catch (error) {
      // Gérer l'erreur
    }
  };
  

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };
  

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Create your account
          </h2>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md shadow-sm space-y-2">
            <div className="flex space-x-2">
              <input
                type="text"
                required
                name="prenom"
                className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="First Name"
                value={formData.prenom}
                onChange={handleChange}
              />
              <input
                type="text"r
                required
                name ="nom"
                className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                placeholder="Last Name"
                value={formData.nom}
                onChange={handleChange}
              />
            </div>
            <input
              type="email"
              required
              name="login"
              className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="Email address"
              value={formData.login}
              onChange={handleChange}
            />
            <input
              type="password"
              required
              name="password"
              className="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
            />
          </div>

          <div>
            <button
              type="submit"
              className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
              onClick={handleSubmit}
            >
              Register
            </button>
          </div>
        </form>
        <div className="text-center">
          <p className="mt-2 text-sm text-gray-600">
            Already have an account?{' '}
            <button 
              onClick={onSwitchToLogin}
              className="font-medium text-blue-600 hover:text-blue-500"
            >
              Sign in
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;