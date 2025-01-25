const Category = require('../models/category');

const createCategory = async (name, promoted) => {
  const category = new Category({ name: name });
  category.promoted = promoted;
  await category.save();
};

const getCategoryById = async (id) => {
    try {
        return await Category.findById(id);
    } catch (error) {
        return null;
    }
};

const getCategories = async () => {
  return await Category.find({});
};

const updateCategory = async (id,name,promoted) => {
  try{
    const category = await find(id);
    if (!category) return null;
    category.name = name;
    category.promoted = promoted
    await category.save();
    return category;
  }
  catch(err){
    return null;
  }
};

const deleteCategory = async (id) => {
  try{
  const category = await getCategoryById(id);
  if (!category) return null;
  
  await category.deleteOne();
  return category;
  }
  catch(err){
    return null;
  }
};

module.exports = { createCategory, getCategoryById, getCategories, updateCategory, deleteCategory };