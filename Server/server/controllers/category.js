const categoryService = require('../services/category');


const createCategory = async (req, res) => {
  try{
    if(!req.body.name){
        return res.status(400).json({ errors: ['Name is required'] });
    }
    await categoryService.createCategory(req.body.name,req.body.promoted);
    res.status(201).json();
  }
  catch(err){
    res.status(400).json({ errors: ['Bad Request'], details: err.message });
  }
};

const getCategories = async (req, res) => {
  res.json(await categoryService.getCategories());
};

const getCategory = async (req, res) => {
try{

  const category = await categoryService.getCategoryById(req.params.id);
  if (!category) {
    return res.status(404).json({ errors: ['Category not found'] });
  }

  res.status(200).json(category);
}
catch(err){
    res.status(400).json({ errors: ['Bad Request'], details: err.message });
}
};
const updateCategory = async (req, res) => {
  try {
    if(!req.body.name){
        return res.status(400).json({ errors: ['Name is required'] });
    }
    const updatedCategory = await categoryService.updateCategory(req.params.id, req.body.name);
    if (!updatedCategory) {
      return res.status(404).json({ errors: ['Category not found'] });
    }

    res.status(204).json();
  } catch (err) {
    res.status(500).json({ errors: ['Internal Server Error'], details: err.message });
  }
};
const deleteCategory = async (req, res) => {
  try {
    const deletedCategory = await categoryService.deleteCategory(req.params.id);
    if (!deletedCategory) {
      return res.status(404).json({ errors: ['Category not found'] });
    }
    res.status(204).json();
  } catch (err) {
    res.status(400).json({ errors: ['Bad Request'], details: err.message });
  }
};


// ...

module.exports = { createCategory, getCategories, getCategory, updateCategory, deleteCategory };
