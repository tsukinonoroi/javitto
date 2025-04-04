ALTER TABLE advertisement
    ALTER COLUMN title SET NOT NULL;

ALTER TABLE advertisement
    ALTER COLUMN description SET NOT NULL;

ALTER TABLE advertisement
    ALTER COLUMN parent_category SET NOT NULL;

ALTER TABLE advertisement
    ALTER COLUMN city SET NOT NULL;
