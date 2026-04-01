// src/pages/transactions/TransactionsList.jsx
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Box,
  TextField,
  InputAdornment,
  Chip,
  IconButton,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import {
  Add as AddIcon,
  Search as SearchIcon,
  Assessment as AssessmentIcon,
  Delete as DeleteIcon,
  TrendingUp as TrendingUpIcon,
  TrendingDown as TrendingDownIcon,
  AccountBalance as AccountBalanceIcon,
  Receipt as ReceiptIcon,
} from '@mui/icons-material';
import { transactionService } from '../../services/transactionService';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import { formatCurrency, formatDate } from '../../utils/formatters';
import toast from 'react-hot-toast';

const TransactionsList = () => {
  const [transactions, setTransactions] = useState([]);
  const [filteredTransactions, setFilteredTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedType, setSelectedType] = useState('ALL');
  const [stats, setStats] = useState({
    totalIncome: 0,
    totalExpense: 0,
    balance: 0,
    transactionCount: 0,
  });
  const navigate = useNavigate();

  const transactionTypes = [
    { value: 'ALL', label: 'Tous' },
    { value: 'ENTREE', label: 'Entrées' },
    { value: 'SORTIE', label: 'Sorties' },
    { value: 'COTISATION', label: 'Cotisations' },
    { value: 'DON', label: 'Dons' },
  ];

  useEffect(() => {
    loadTransactions();
  }, [selectedType]);

  useEffect(() => {
    const filtered = (Array.isArray(transactions) ? transactions : []).filter(transaction =>
      transaction.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      transaction.category?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      transaction.memberName?.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredTransactions(filtered);
  }, [transactions, searchTerm]);

  const loadTransactions = async () => {
    try {
      const response = selectedType === 'ALL'
        ? await transactionService.getTransactions()
        : await transactionService.getTransactionsByType(selectedType);

      const data = response.data || [];
      const transactionArray = Array.isArray(data) ? data : [];
      setTransactions(transactionArray);
      calculateStats(transactionArray);
    } catch (error) {
      console.error('Error loading transactions:', error);
      toast.error('Erreur lors du chargement des transactions');
      setTransactions([]);
      calculateStats([]);
    } finally {
      setLoading(false);
    }
  };

  const calculateStats = (transactions) => {
    const transactionArray = Array.isArray(transactions) ? transactions : [];
    const stats = {
      transactionCount: transactionArray.length,
      totalIncome: transactionArray
        .filter(t => ['ENTREE', 'COTISATION', 'DON'].includes(t.type))
        .reduce((sum, t) => sum + (t.amount || 0), 0),
      totalExpense: transactionArray
        .filter(t => t.type === 'SORTIE')
        .reduce((sum, t) => sum + (t.amount || 0), 0),
      balance: 0,
    };
    stats.balance = stats.totalIncome - stats.totalExpense;
    setStats(stats);
  };

  const getTypeColor = (type) => {
    switch (type) {
      case 'ENTREE': return 'primary';
      case 'SORTIE': return 'error';
      case 'COTISATION': return 'secondary';
      case 'DON': return 'success';
      default: return 'default';
    }
  };

  const getTypeIcon = (type) => {
    switch (type) {
      case 'ENTREE': return 'arrow_downward';
      case 'SORTIE': return 'arrow_upward';
      case 'COTISATION': return 'payments';
      case 'DON': return 'volunteer_activism';
      default: return 'attach_money';
    }
  };

  const handleDeleteTransaction = async (transaction) => {
    if (window.confirm(`Supprimer la transaction "${transaction.description}" ?`)) {
      try {
        await transactionService.deleteTransaction(transaction.id);
        toast.success('Transaction supprimée');
        loadTransactions();
      } catch (error) {
        console.error('Error deleting transaction:', error);
        toast.error('Erreur lors de la suppression');
      }
    }
  };

  const handleAddTransaction = () => {
    navigate('/transactions/new');
  };

  const handleViewReport = () => {
    navigate('/reports');
  };

  if (loading) {
    return <LoadingSpinner message="Chargement des transactions..." />;
  }

  return (
    <Container maxWidth="xl">
      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" component="h1">
          Gestion des Transactions
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            variant="outlined"
            startIcon={<AssessmentIcon />}
            onClick={handleViewReport}
          >
            Rapport Financier
          </Button>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={handleAddTransaction}
          >
            Nouvelle Transaction
          </Button>
        </Box>
      </Box>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <TrendingUpIcon color="success" />
            <Box>
              <Typography variant="body2" color="text.secondary">
                Total Entrées
              </Typography>
              <Typography variant="h6" color="success.main">
                {formatCurrency(stats.totalIncome)}
              </Typography>
            </Box>
          </Paper>
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <TrendingDownIcon color="error" />
            <Box>
              <Typography variant="body2" color="text.secondary">
                Total Sorties
              </Typography>
              <Typography variant="h6" color="error.main">
                {formatCurrency(stats.totalExpense)}
              </Typography>
            </Box>
          </Paper>
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <AccountBalanceIcon color={stats.balance >= 0 ? 'success' : 'error'} />
            <Box>
              <Typography variant="body2" color="text.secondary">
                Balance
              </Typography>
              <Typography
                variant="h6"
                color={stats.balance >= 0 ? 'success.main' : 'error.main'}
              >
                {formatCurrency(stats.balance)}
              </Typography>
            </Box>
          </Paper>
        </Grid>
        <Grid xs={12} sm={6} md={3}>
          <Paper sx={{ p: 2, display: 'flex', alignItems: 'center', gap: 2 }}>
            <ReceiptIcon color="primary" />
            <Box>
              <Typography variant="body2" color="text.secondary">
                Transactions
              </Typography>
              <Typography variant="h6" color="primary.main">
                {stats.transactionCount}
              </Typography>
            </Box>
          </Paper>
        </Grid>
      </Grid>

      {/* Filters and Table */}
      <Paper sx={{ p: 2 }}>
        <Box sx={{ display: 'flex', gap: 2, mb: 2 }}>
          <FormControl sx={{ minWidth: 120 }}>
            <InputLabel>Type</InputLabel>
            <Select
              value={selectedType}
              label="Type"
              onChange={(e) => setSelectedType(e.target.value)}
            >
              {transactionTypes.map((type) => (
                <MenuItem key={type.value} value={type.value}>
                  {type.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            sx={{ flex: 1 }}
            variant="outlined"
            placeholder="Rechercher par description, catégorie..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
        </Box>

        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Date</TableCell>
                <TableCell>Type</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Catégorie</TableCell>
                <TableCell>Montant</TableCell>
                <TableCell>Membre</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredTransactions.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    Aucune transaction trouvée
                  </TableCell>
                </TableRow>
              ) : (
                filteredTransactions.map((transaction) => (
                  <TableRow key={transaction.id}>
                    <TableCell>{formatDate(transaction.transactionDate)}</TableCell>
                    <TableCell>
                      <Chip
                        label={transaction.type}
                        color={getTypeColor(transaction.type)}
                        size="small"
                        icon={<span style={{ fontSize: '16px' }}>{getTypeIcon(transaction.type)}</span>}
                      />
                    </TableCell>
                    <TableCell>{transaction.description || '-'}</TableCell>
                    <TableCell>{transaction.category || '-'}</TableCell>
                    <TableCell>
                      <Typography
                        variant="body2"
                        color={transaction.type === 'SORTIE' ? 'error.main' : 'success.main'}
                      >
                        {transaction.type === 'SORTIE' ? '-' : '+'}
                        {formatCurrency(transaction.amount || 0)}
                      </Typography>
                    </TableCell>
                    <TableCell>{transaction.memberName || '-'}</TableCell>
                    <TableCell>
                      <IconButton
                        size="small"
                        color="error"
                        onClick={() => handleDeleteTransaction(transaction)}
                        title="Supprimer"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Container>
  );
};

export default TransactionsList;