namespace Project.DesignPatterns.Structural;

public abstract class AFileComponent
{
    public string Name { get; set; } = string.Empty;
    public int Size { get; set; }

    public virtual long GetTotalSize() => Size;
    public abstract IEnumerable<AFileComponent> GetChildrens();
}

public class File : AFileComponent
{
    public override IEnumerable<AFileComponent> GetChildrens()
    {
        return Array.Empty<AFileComponent>().AsEnumerable(); 
    }
}

public class Directory : AFileComponent
{
    public List<AFileComponent> Childrens { get; } = [];  
    public override long GetTotalSize() =>  Size + Childrens.Sum(Children => Children.Size);
    public override IEnumerable<AFileComponent> GetChildrens() => Childrens;
}