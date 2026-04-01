// src/App.jsx
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import { ThemeProvider, createTheme } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';

import Login from './pages/auth/Login';
import Layout from './components/layout/Layout';
import Dashboard from './pages/dashboard/Dashboard';
import MembersList from './pages/members/MembersList';
import AddMember from './pages/members/AddMember';
import MemberDetail from './pages/members/MemberDetail';
import TransactionsList from './pages/transactions/TransactionsList';
import AddTransaction from './pages/transactions/AddTransaction';
import ContributionsList from './pages/contributions/ContributionsList';
import ManageContributions from './pages/contributions/ManageContributions';
import FinancialReport from './pages/reports/FinancialReport';
import PrivateRoute from './components/PrivateRoute';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <BrowserRouter>
          <Toaster position="top-right" />
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/" element={<PrivateRoute><Layout /></PrivateRoute>}>
              <Route index element={<Navigate to="/dashboard" replace />} />
              <Route path="dashboard" element={<Dashboard />} />
              <Route path="members" element={<MembersList />} />
              <Route path="members/new" element={<AddMember />} />
              <Route path="members/:id" element={<MemberDetail />} />
              <Route path="transactions" element={<TransactionsList />} />
              <Route path="transactions/new" element={<AddTransaction />} />
              <Route path="contributions" element={<ContributionsList />} />
              <Route path="contributions/manage" element={<ManageContributions />} />
              <Route path="reports" element={<FinancialReport />} />
            </Route>
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;